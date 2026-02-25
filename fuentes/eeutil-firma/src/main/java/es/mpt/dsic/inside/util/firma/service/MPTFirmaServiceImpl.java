/*
 * Copyright (C) 2025, Gobierno de España This program is licensed and may be used, modified and
 * redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at
 * your option) any later version as soon as they are approved by the European Commission. Unless
 * required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and more details. You
 * should have received a copy of the EUPL1.1 license along with this program; if not, you may find
 * it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */

package es.mpt.dsic.inside.util.firma.service;

import java.io.File;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.PasswordCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.core.signers.AOSignerFactory;
import es.gob.afirma.core.signers.CounterSignTarget;
import es.gob.afirma.keystores.AOKeyStore;
import es.gob.afirma.keystores.AOKeyStoreManager;
import es.gob.afirma.keystores.AOKeyStoreManagerFactory;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.gob.afirma.signers.padestri.client.AOPDFTriPhaseSigner;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.util.Constantes;
import es.mpt.dsic.inside.util.FlagFirstTime;
import es.mpt.dsic.inside.util.firma.TextosErroresFirma;
import es.mpt.dsic.inside.util.firma.ext.SignatureUtil;
import es.mpt.dsic.inside.util.firma.model.DatosFirma;
import es.mpt.dsic.inside.util.firma.model.PeticionFirmaFichero;
import es.mpt.dsic.inside.util.firma.model.RespuestaFirmaFichero;
import es.mpt.dsic.inside.util.firma.util.UtilFirma;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.pdf.PdfEncr;



/**
 * Clase con operaciones del modulo eeutil-firma
 * 
 * @author miguel.moral
 *
 */
@Service("mptFirmaService")
public class MPTFirmaServiceImpl implements MPTFirmaService {
  protected static final Log logger = LogFactory.getLog(MPTFirmaServiceImpl.class);

  private UtilFirma utilFirma;

  private TextosErroresFirma textosErroresFirma;

  private PasswordCallback pc = new PasswordCallback(">", false);

  @Override
  public RespuestaFirmaFichero firmaFichero(AppInfo aplicacion, PeticionFirmaFichero peticion)
      throws MPTFirmaException {

    byte[] datos = null;
    byte[] firma = null;
    PrivateKeyEntry pke = null;
    // Fuerza el proveedor de servicios de seguridad segun especificacion de @firma (si no se fuerza
    // la firma aparece invalida en valide y en @firma.
    if (FlagFirstTime.isFirstTimeLoadProvidedBouncy()) {
      // Provider provider = Security.getProvider("BC");
      // if (provider != null) {
      Security.removeProvider("BC");
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      // }
      FlagFirstTime.setFirstTimeLoadProvidedBouncy(false);
    }

    try {
      textosErroresFirma = TextosErroresFirma.getInstance();

      if (!peticion.verificaContenido()) {
        throw new MPTFirmaDatosIncorrectosException(
            this.textosErroresFirma.getProperty(Constantes.DATOS_INCORRECTOS_PROP));
      }
      datos = peticion.getContenido();

      if (peticion.getAlgoritmo() == null) {
        peticion.setAlgoritmo(
            aplicacion.getPropiedades().get(Constantes.ALGORITMO_FIRMA_DEFECTO_PROP_BBDD));
      }
      if (peticion.getFormato() == null) {
        peticion.setFormato(
            aplicacion.getPropiedades().get(Constantes.FORMATO_FIRMA_DEFECTO_PROP_BBDD));
      }
      if (peticion.getModo() == null) {
        peticion.setModo(aplicacion.getPropiedades().get(Constantes.MODO_FIRMA_DEFECTO_PROP_BBDD));
      }

      AOKeyStoreManager storeManager = getStoreManager(aplicacion);
      pke = getClavePrivada(storeManager, aplicacion);
      String operacion = getOperacion(peticion);


      firma = firmaFichero(pke, peticion, operacion);

    } catch (Exception e) {
      throw new MPTFirmaException("Error al ejecutar firmarFichero. " + e.getMessage(), e);
    }


    RespuestaFirmaFichero respuesta = new RespuestaFirmaFichero();
    respuesta.setContenido(datos);

    respuesta.setFormato(peticion.getFormato());
    respuesta.setModo(peticion.getModo());
    respuesta.setAlgoritmo(peticion.getAlgoritmo());
    respuesta.setFirma(firma);
    DatosFirma datfir = getDatosFirmante(pke);
    respuesta.setDatosFirma(datfir);


    return respuesta;
  }


  private byte[] firmaFichero(PrivateKeyEntry pke, PeticionFirmaFichero pet, String operacion)
      throws MPTFirmaException {

    logger.debug("Firmando fichero... init");

    try {
      textosErroresFirma = TextosErroresFirma.getInstance();
    } catch (EeutilException e) {
      throw new MPTFirmaException(
          "No se han podido encontrar las propiedades en la clase TextosErroresFirma "
              + e.getMessage(),
          e);
    }

    byte[] res2 = null;

    try {
      Properties propiedadesFirma = new Properties();
      String formato = utilFirma.checkFormato(pet.getFormato(), propiedadesFirma);
      logger.debug("Propiedades de la firma: Formato, Modo, Algoritmo, Operacion: " + formato + ", "
          + pet.getModo() + ", " + pet.getAlgoritmo() + ", " + operacion);

      formato = generatePropiedadesFirma(pet, propiedadesFirma, formato);

      AOSigner signer = AOSignerFactory.getSigner(formato);

      if (signer == null) {
        String msg = textosErroresFirma.getProperty(Constantes.FORMATO_FIRMA_INCORRECTO_PROP);
        msg = msg.replace("#data#", pet.getFormato());
        throw new MPTFirmaOperacionFirmaException(msg);
      }

      // si el formato es PDF, verificamos que no este protegido.
      // preferimos penalizar a dejar hilos colgados en produccion, por eso siempre va a validar
      // para cada firma si es un pdf protegido,
      // no nos podemos fiar del formato, por eso comentamos el if
      if ((signer instanceof AOPDFSigner || signer instanceof AOPDFTriPhaseSigner)
          && PdfEncr.isProtectedPdf(pet.getContenido())) {
        // si el documento esta protegido, error

        throw new MPTFirmaException(
            "Error al firmar fichero. El fichero pdf tiene contrase�a y no se puede procesar");
      }



      if (Constantes.OPERACION_FIRMA.contentEquals(operacion)) {
        res2 = signer.sign(pet.getContenido(), pet.getAlgoritmo(), pke.getPrivateKey(),
            pke.getCertificateChain(), propiedadesFirma);
      } else if (Constantes.OPERACION_COFIRMA.contentEquals(operacion)) {
        res2 = signer.cosign(pet.getContenido(), pet.getAlgoritmo(), pke.getPrivateKey(),
            pke.getCertificateChain(), propiedadesFirma);
      } else if (Constantes.OPERACION_CONTRAFIRMAR_HOJAS.contentEquals(operacion)) {
        res2 = signer.countersign(pet.getContenido(), pet.getAlgoritmo(), CounterSignTarget.LEAFS,
            null, pke.getPrivateKey(), pke.getCertificateChain(), propiedadesFirma);
      } else if (Constantes.OPERACION_CONTRAFIRMAR_ARBOL.contentEquals(operacion)) {
        res2 = signer.countersign(pet.getContenido(), pet.getAlgoritmo(), CounterSignTarget.TREE,
            null, pke.getPrivateKey(), pke.getCertificateChain(), propiedadesFirma);
      }

    } catch (Exception e) {

      throw new MPTFirmaOperacionFirmaException(
          textosErroresFirma.getProperty(Constantes.ERROR_PROCESO_FIRMA_PROP) + ". "
              + e.getMessage(),
          e);
    }

    if (res2 == null || res2.length == 0) {
      throw new MPTFirmaOperacionFirmaException(
          textosErroresFirma.getProperty(Constantes.ERROR_PROCESO_FIRMA_PROP)
              + ". El contenido a firmar esta vacio.");
    }
    logger.debug("Firmando fichero... end");
    return res2;
  }


  /**
   * @param pet
   * @param propiedadesFirma
   * @param formato
   * @return
   * @throws EeutilException
   */
  private String generatePropiedadesFirma(PeticionFirmaFichero pet, Properties propiedadesFirma,
      String formato) throws EeutilException {
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PROPIEDADES DE FIRMA XADES MANIFEST
    // "format=XAdES Externally Detached"
    // "precalculatedHashAlgorithm=SHA-512"
    // "useManifest=true"
    // "uri=urn:id:nombrefichero"
    //
    // *Para no cambiar el servicio el partametro nodeToSign en firma XADES MANIFEST LO USAMOS PARA
    // METER EL NOMBRE DE FICHERO.
    //
    // Si nos envian solicitud de firma manifest acomodamos los parametros para realizar dicha firma
    // la firma xades manifest es una firma detached y hay que cambiar el formato a uno que exista

    if (SignatureUtil.esXadesManifest(pet.getFormato())) // traducir a formato reconocido y valido
    {
      formato = SignatureUtil.traducirAFormatoValido(pet.getFormato());

      // Segundo: sumamos las properties necesarias para la firma XADES manifest estas son las
      // extraparams
      propiedadesFirma.setProperty("format", "XAdES Externally Detached");
      propiedadesFirma.setProperty("mode", pet.getModo().toLowerCase());
      propiedadesFirma.setProperty("precalculatedHashAlgorithm", "SHA-512");
      propiedadesFirma.setProperty("useManifest", "true");
      propiedadesFirma.setProperty("uri", "urn:id:" + pet.getParams());
    } else {
      propiedadesFirma.setProperty("format", formato);
      propiedadesFirma.setProperty("mode", pet.getModo().toLowerCase());

      // si es pdf
      if ("Adobe PDF".equals(formato)) {
        propiedadesFirma.setProperty("headless", "true");
        propiedadesFirma.setProperty("allowSigningCertifiedPdfs", "true");
      }

      // el parametro del id del nodo a firmar
      if (pet.getParams() != null && !pet.getParams().trim().equals(""))
        propiedadesFirma.setProperty("nodeToSign", pet.getParams());
    }
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    return formato;
  }


  private AOKeyStoreManager getStoreManager(AppInfo app) throws MPTFirmaException {
    logger.debug("Obteniendo keystoreManager...: init");
    try {
      textosErroresFirma = TextosErroresFirma.getInstance();
    } catch (EeutilException e) {
      throw new MPTFirmaException(e.getMessage(), e);
    }

    File certfile = null;

    try {
      certfile = ResourceUtils
          .getFile(app.getPropiedades().get(Constantes.RUTA_ALMACEN_CERTIFICADO_PROP_BBDD));
      logger.debug("Encontrado almacen de certificados");

      if (!certfile.exists()) {
        throw new MPTFirmaOpcionesCertificadoException(
            textosErroresFirma.getProperty(Constantes.CERT_NO_ENCONTRADO_PROP));
      }
    } catch (Exception e) {
      throw new MPTFirmaOpcionesCertificadoException(
          textosErroresFirma.getProperty(Constantes.CERT_NO_ENCONTRADO_PROP) + ". "
              + e.getMessage(),
          e);
    }

    pc.setPassword(
        app.getPropiedades().get(Constantes.PASS_ALMACEN_CERTIFICADO_PROP_BBDD).toCharArray());
    try {
      AOKeyStoreManager ksm = AOKeyStoreManagerFactory.getAOKeyStoreManager(
          AOKeyStore
              .valueOf(app.getPropiedades().get(Constantes.TIPO_ALMACEN_CERTIFICADO_PROP_BBDD)),
          certfile.getAbsolutePath(), null, pc, null);

      if (ksm == null) {
        throw new MPTFirmaOpcionesCertificadoException(
            textosErroresFirma.getProperty(Constantes.CERT_NO_ABIERTO_PROP));
      }

      logger.debug("Obteniendo keystoreManager...: end");
      return ksm;
    } catch (Exception e) {

      throw new MPTFirmaOpcionesCertificadoException(
          Constantes.CERT_NO_ABIERTO_PROP + ". " + e.getMessage(), e);
    }

  }

  private PrivateKeyEntry getClavePrivada(AOKeyStoreManager ksm, AppInfo app)
      throws MPTFirmaException {
    logger.debug("getClavePrivada... init");
    TextosErroresFirma oTxtErroresFirmas = null;
    try {

      oTxtErroresFirmas = TextosErroresFirma.getInstance();

      String alias = app.getPropiedades().get(Constantes.ALIAS_CERTIFICADO_PROP_BBDD);
      PrivateKeyEntry pek = ksm.getKeyEntry(alias);

      if (pek == null) {
        pek = ksm.getKeyEntry(alias.toLowerCase());
        if (pek == null) {
          pek = ksm.getKeyEntry(alias.toUpperCase());
        }
        if (pek == null) {

          if (ksm.getAliases() != null && ksm.getAliases().length == 1) {
            pek = ksm.getKeyEntry(ksm.getAliases()[0]);
          }

          if (pek == null) {
            String propiedad = oTxtErroresFirmas.getProperty(Constantes.PK_NO_ABIERTO_PROP);
            throw new MPTFirmaOpcionesCertificadoException(propiedad);
          }
        }
      }
      logger.debug("getClavePrivada... end");
      return pek;
    } catch (Exception e) {
      String propiedad =
          oTxtErroresFirmas != null ? oTxtErroresFirmas.getProperty(Constantes.PK_NO_ABIERTO_PROP)
              : null;
      throw new MPTFirmaOpcionesCertificadoException(propiedad + ". " + e.getMessage(), e);
    }
  }


  private DatosFirma getDatosFirmante(PrivateKeyEntry pp) throws MPTFirmaException {
    logger.debug("Obteniendo datos firmante... init");
    TextosErroresFirma oTxtErroresFirmas = null;
    try {
      oTxtErroresFirmas = TextosErroresFirma.getInstance();
      DatosFirma datfir = new DatosFirma();
      X509Certificate t = (X509Certificate) pp.getCertificate();
      Map<String, String> datosPrin = formateaCadenaPrincipal(t.getSubjectDN().toString());
      Map<String, String> datosRaiz = formateaCadenaPrincipal(t.getIssuerDN().toString());
      String nombreFirmante = datosPrin.get("CN");
      String numeroIdentificacion = datosPrin.get("SERIALNUMBER");
      String mailFirmante = datosPrin.get("EMAILADDRESS");
      String entidadCertificadora = datosRaiz.get("CN");
      datfir.setNombreFirmante(nombreFirmante);
      datfir.setNumeroIdentificacion(numeroIdentificacion);
      datfir.setFechaFirma(new Date());
      datfir.setMailFirmante(mailFirmante);
      datfir.setEntidadCertificadora(entidadCertificadora);

      logger.debug("Obteniendo datos firmante... end");
      return datfir;
    }

    catch (Exception e) {

      String propiedad =
          oTxtErroresFirmas != null ? oTxtErroresFirmas.getProperty(Constantes.PK_NO_ABIERTO_PROP)
              : null;
      throw new MPTFirmaOpcionesCertificadoException(propiedad + ". " + e.getMessage(), e);
    }
  }


  private Map<String, String> formateaCadenaPrincipal(String principal) throws EeutilException {

    Map<String, String> datos = new HashMap<>();
    try {

      String[] dat = StringUtils.tokenizeToStringArray(principal, ",", true, true);
      for (String dato : dat) {
        String[] attr = StringUtils.tokenizeToStringArray(dato, "=", true, true);
        datos.put(attr[0], attr[1]);
      }
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return datos;

  }



  private String getOperacion(PeticionFirmaFichero peticion) throws EeutilException {
    String operacion = Constantes.OPERACION_FIRMA;


    try {

      String formato = SignatureUtil.traducirAFormatoValido(peticion.getFormato());

      boolean isSigned = SignatureUtil.checkIsSign(peticion.getContenido(), formato);

      if (isSigned) {
        if (peticion.isCofirmarSiFirmado()) {
          operacion = Constantes.OPERACION_COFIRMA;
        } else {
          operacion = Constantes.OPERACION_CONTRAFIRMAR_HOJAS;
        }
      }

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return operacion;
  }

  /**
   * get
   * 
   * @return devuelve el objeto de la clase UtilFirma.
   */
  public UtilFirma getUtilFirma() {
    return utilFirma;
  }

  /**
   * set del objeto de la clase UtilFirma.
   * 
   * @param utilFirma objeto para setear.
   */
  public void setUtilFirma(UtilFirma utilFirma) {
    this.utilFirma = utilFirma;
  }

}
