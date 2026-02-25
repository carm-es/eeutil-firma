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

package es.mpt.dsic.inside.util.firma.ext;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import es.gob.afirma.core.AOInvalidFormatException;
import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.core.signers.AOSignerFactory;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.io.IOUtil;
import es.mpt.dsic.inside.utils.xml.XMLUtil;

/**
 * Clase de utilidades de firma.
 * 
 * @author miguel.moral
 *
 */
public class SignatureUtil {

  // private constructor for hidden public
  private SignatureUtil() {

  }

  protected static final Log logger = LogFactory.getLog(SignatureUtil.class);

  /**
   * Metodo para saber si es firma, Nunca lanzara una excepcion.
   * 
   * @param source objeto.
   * @param formato el formato.
   * @return isSigned=true (es firma, isSigned=false (no es firma).Nunca lanzara una excepcion.
   */
  public static boolean checkIsSign(Object source, String formato) {
    logger.debug("checkIsSign init");
    boolean parseError = false;
    boolean isSigned = false;

    Document dom = null;
    try {
      dom = XMLUtil.getDOMDocument(source, true);

      // Si se produce alguna excepcion al parsear, significara que no es un XML y por
      // tanto no sera una firma XML.
    } catch (ParserConfigurationException | SAXException | IOException se) {
      parseError = true;
    } catch (Exception e) {
      // No haremos nada, nunca se lanzara excepcion.
    }

    // Si se ha podido parsear el documento, comprobamos que se trata de una firma
    // XML.
    if (!parseError && dom != null) {
      logger.debug("not parseError");

      isSigned = isDocumentXMLSign(dom);
    }
    // si no se ha podido parsear comprobamos que es otro tipo de firma
    else {
      try {
        logger.debug("parseError");
        isSigned = isOtherSign(IOUtil.getBytesFromObject(source), formato);
      } catch (Exception e) {
        logger.error("Error al recuperar firma:Gestion a posteriori." + e.getMessage(), e);
        isSigned = false;
      }
    }
    logger.debug("checkIsSign end");
    return isSigned;
  }

  /**
   * Consideraremos que el arbol DOM representa a una firma XML con el documento implicito cuando
   * contenga el primer nodo con el documento firmado y, al mismo nivel, un nodo Signature.
   * 
   * @param dom arbol DOM de un documento XML.
   * @return true si representa una firma XML, false en caso contrario.
   */
  private static boolean isDocumentXMLSign(Document dom) {

    boolean esXMLSign = false;

    try {

      logger.debug("isDocumentXMLSign init");
      // comprobamos que tenga el nodo con el documento firmado y el nodo Signature.
      // Estos dos nodos tienen que colgar del nodo padre del documento.
      Element elementRoot = dom.getDocumentElement();

      // Obtenemos el primer hijo del nodo Root.
      Node child = elementRoot.getFirstChild() != null ? elementRoot.getFirstChild() : elementRoot;
      boolean lastChild = false;
      boolean tieneDocFirmado = false;
      boolean tieneSignature = false;

      while (!lastChild && !esXMLSign && child != null) {
        // Si no hemos encontrado el nodo que contiene al documento firmado, miramos si
        // es el actual.
        if (!tieneDocFirmado) {
          tieneDocFirmado = XMLUtil.contieneIdEncoding(child);
        }
        // Si no hemos encontrado el nodo que contiene la firma, miramos si es el
        // actual.
        if (!tieneSignature) {
          /**
           * tieneSignature = child.getNodeName().equalsIgnoreCase("ds:Signature");
           */
          tieneSignature = child.getLocalName() != null && child.getLocalName().equals("Signature");
        }

        if (child == elementRoot.getLastChild()) {
          lastChild = true;
        } else {
          child = child.getNextSibling();
        }
        esXMLSign = tieneDocFirmado && tieneSignature;
      }
      logger.debug("isDocumentXMLSign end");

    } catch (Exception e) {
      esXMLSign = false;
    }

    return esXMLSign;
  }

  /**
   * Comprueba si unos bytes se corresponden con los de una firma electŕonica (que no sea firma
   * PDF).
   * 
   * @param bytesFirma
   * @return true si lo es, false en caso contrario.
   * @throws IOException
   * @throws AOInvalidFormatException
   */
  private static boolean isOtherSign(byte[] bytesFirma, String format) {

    es.gob.afirma.core.signers.AOSigner signer = null;

    try {

      logger.debug("isOtherSign init");

      signer = obtenerSigner(format);



      if (signer == null) {
        return false;
      }

      return (!(signer instanceof AOPDFSigner) && signer.isSign(bytesFirma));

    } catch (Exception e) {
      return false;
    }

  }

  /**
   * Se obtiene un objeto para manipular la firma.
   * 
   * @param bytes de la firma
   * @return Instancia de un objeto para manipular la firma.
   * @throws IOException
   */
  private static AOSigner obtenerSigner(String format) {
    AOSigner signer = null;

    try {
      signer = AOSignerFactory.getSigner(format);
    } catch (Exception e) {
      // si hay algun tipo de error el signer se devuelve a null
      signer = null;
    }
    return signer;
  }

  /**
   * Traduce de Xades Manifest a Xades Detached si fuera el caso.
   * 
   * @param format el formato.
   * @return el formato traducido si aplica.
   * @throws EeutilException excepcion generica de eeutils.
   */
  public static String traducirAFormatoValido(String format) throws EeutilException {
    String formatoValido = null;

    try {

      formatoValido = format;

      if ("XAdES Manifest".equalsIgnoreCase(format.trim())) {
        formatoValido = format.toLowerCase().replace("manifest", "Detached");
      }

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return formatoValido;
  }

  /**
   * Pregunta si una firma es de tipo XadesManifest.
   * 
   * @param format el formato.
   * @return true si lo es, false si no lo es.
   * @throws EeutilException excepcion generica de eeutils.
   */
  public static boolean esXadesManifest(String format) throws EeutilException {
    boolean isXadesManifest = false;

    try {

      if ("XAdES Manifest".equalsIgnoreCase(format.trim())) {
        isXadesManifest = true;
      }

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return isXadesManifest;
  }

}
