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

package es.mpt.dsic.inside.ws.service.impl;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.mpt.dsic.inside.exception.ConstantsMDC;
import es.mpt.dsic.inside.security.context.AplicacionContext;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.service.FirmaService;
import es.mpt.dsic.inside.util.SecurityUtils;
import es.mpt.dsic.inside.util.firma.model.PeticionFirmaFichero;
import es.mpt.dsic.inside.util.firma.model.RespuestaFirmaFichero;
import es.mpt.dsic.inside.util.firma.service.MPTFirmaException;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.file.FileUtil;
import es.mpt.dsic.inside.utils.misc.MiscUtil;
import es.mpt.dsic.inside.ws.service.model.ContenidoSalidaErronea;
import es.mpt.dsic.inside.ws.service.model.ContenidoSalidaFicheroCorrecta;
import es.mpt.dsic.inside.ws.service.model.ContenidoSalidaFicheroCorrectaMtom;
import es.mpt.dsic.inside.ws.service.model.DatosEntradaFichero;
import es.mpt.dsic.inside.ws.service.model.DatosEntradaFicheroMtom;
import es.mpt.dsic.inside.ws.service.model.DatosSalida;

/**
 * Clase de negocio del modulo eeutil-firma.
 * 
 * @author miguel.moral
 *
 */
@Component
public class EeUtilFirmarServiceImplBusiness {

  private static final String ERROR = "ERROR";

  protected static final Log logger = LogFactory.getLog(EeUtilFirmarServiceImplBusiness.class);

  @Autowired(required = false)
  private AplicacionContext aplicacionContext;

  @Autowired
  FirmaService firmaService;

  @Autowired
  private es.mpt.dsic.inside.util.firma.service.MPTFirmaService mptFirmaService;

  /**
   * metodo de negocio para realizar la firma de fichero.
   * 
   * @param entrada datos de firma.
   * @return la firma o error formateado.
   */
  public DatosSalida firmaFichero(DatosEntradaFichero entrada) {
    MDC.put(ConstantsMDC.EXTRA_PARAM, entrada.printData());
    DatosSalida salida = null;
    try {
      RespuestaFirmaFichero respuesta =
          firmaService.firmarFichero(aplicacionContext.getAplicacionInfo(), entrada);
      salida = new DatosSalida("OK");
      ContenidoSalidaFicheroCorrecta contenido = new ContenidoSalidaFicheroCorrecta(respuesta);
      salida.setSalida(contenido);
    } catch (MPTFirmaException ex) {
      salida = generarDatosSalidaMPTException(ex);
    } catch (Exception ex) {
      salida = generarDatosSalidaException(ex);
    }
    return salida;

  }

  /**
   * @param ex
   * @return
   */
  private DatosSalida generarDatosSalidaException(Exception ex) {
    DatosSalida salida;
    logger.error(ex.getMessage(), ex);
    MPTFirmaException mptEr = new MPTFirmaException(ex);
    salida = new DatosSalida(ERROR);
    ContenidoSalidaErronea contenidoError = new ContenidoSalidaErronea();
    contenidoError.setMensaje(mptEr.getTipoError());
    contenidoError.setCausa(mptEr.getMessage());
    salida.setSalida(contenidoError);
    return salida;
  }

  /**
   * @param ex
   * @return
   */
  private DatosSalida generarDatosSalidaMPTException(MPTFirmaException ex) {
    DatosSalida salida;
    logger.error(ex.getMessage(), ex);
    salida = new DatosSalida(ERROR);
    ContenidoSalidaErronea contenidoError = new ContenidoSalidaErronea();
    contenidoError.setMensaje(ex.getTipoError());
    contenidoError.setCausa(ex.getMessage());
    salida.setSalida(contenidoError);
    return salida;
  }

  /**
   * metodo para realizar la firma en un nodo predefinido.
   * 
   * @param entrada datos a firmar.
   * @return la firma o error formateado.
   */
  public DatosSalida firmaFicheroWithPropertie(DatosEntradaFichero entrada) {
    MDC.put(ConstantsMDC.EXTRA_PARAM, entrada.printData());
    DatosSalida salida = null;
    try {

      AppInfo aplicacion = aplicacionContext.getAplicacionInfo();
      PeticionFirmaFichero peticion = new PeticionFirmaFichero(entrada);
      RespuestaFirmaFichero respuesta = mptFirmaService.firmaFichero(aplicacion, peticion);
      salida = new DatosSalida("OK");
      ContenidoSalidaFicheroCorrecta contenido = new ContenidoSalidaFicheroCorrecta(respuesta);
      salida.setSalida(contenido);
    } catch (MPTFirmaException ex) {
      salida = generarDatosSalidaMPTException(ex);
    } catch (Exception ex) {
      salida = generarDatosSalidaException(ex);
    }
    return salida;

  }

  /**
   * metodo de negocio para realizar la firma de fichero en mtom.
   * 
   * @param entrada datos de firma.
   * @return la firma o error formateado.
   */
  public DatosSalida firmaFicheroMtom(DatosEntradaFicheroMtom entrada) {
    DatosSalida salida = null;
    MDC.put(ConstantsMDC.EXTRA_PARAM, entrada.printData());
    try {
      if (entrada.getAlgoritmoHuella() != null
          && StringUtils.isNotEmpty(entrada.getAlgoritmoHuella().getValue().value())
          && StringUtils.isNotEmpty(entrada.getHuellaDigital())) {
        // validacion de hash
        SecurityUtils.validateAlgorithm(entrada.getAlgoritmoHuella().getValue().value(),
            entrada.getContenido().getInputStream(), entrada.getHuellaDigital());
      }

      RespuestaFirmaFichero respuesta =
          firmaService.firmarFichero(aplicacionContext.getAplicacionInfo(),
              convertDatosEntradaFicheroMtomToDatosEntradaFichero(entrada));
      salida = new DatosSalida("OK");

      ContenidoSalidaFicheroCorrectaMtom contenido =
          new ContenidoSalidaFicheroCorrectaMtom(respuesta);

      contenido.setAlgoritmoHashContenido(MiscUtil.ALGORITMO_HASH_MD5);
      contenido.setHashContenido(MiscUtil.generateHash(MiscUtil.ALGORITMO_HASH_MD5,
          contenido.getContenido().getInputStream()));
      contenido.setAlgoritmoHashFirma(MiscUtil.ALGORITMO_HASH_MD5);
      contenido.setHashFirma(MiscUtil.generateHash(MiscUtil.ALGORITMO_HASH_MD5,
          contenido.getFirma().getInputStream()));
      salida.setSalida(contenido);
    } catch (SecurityException ex) {
      logger.error(ex.getMessage(), ex);
      salida = new DatosSalida(ERROR);
      ContenidoSalidaErronea contenidoError = new ContenidoSalidaErronea();
      contenidoError.setMensaje("Error en Hash");
      contenidoError.setCausa(ex.getMessage());
      salida.setSalida(contenidoError);
    } catch (MPTFirmaException ex) {
      salida = generarDatosSalidaMPTException(ex);
    } catch (Exception ex) {
      salida = generarDatosSalidaException(ex);
    }
    return salida;

  }


  private DatosEntradaFichero convertDatosEntradaFicheroMtomToDatosEntradaFichero(
      DatosEntradaFicheroMtom entrada) throws EeutilException {
    InputStream stream = null;
    try {
      DatosEntradaFichero datos = new DatosEntradaFichero();
      datos.setAlgoritmoFirma(entrada.getAlgoritmoFirma());
      stream = entrada.getContenido().getInputStream();
      datos.setContenido(IOUtils.toByteArray(stream));
      datos.setModoFirma(entrada.getModoFirma());
      datos.setFormatoFirma(entrada.getFormatoFirma());
      datos.setCofirmarSiFirmado(entrada.isCofirmarSiFirmado());
      return datos;
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    } finally {
      if (stream != null) {
        FileUtil.close(stream);

      }
    }
  }


}
