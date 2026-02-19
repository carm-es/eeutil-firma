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

package es.mpt.dsic.inside.util.firma.util;

import java.util.Properties;

import es.mpt.dsic.inside.util.Constantes;
import es.mpt.dsic.inside.utils.exception.EeutilException;

/**
 * Clase que incluye un procesador para traducir de formato a propiedades de firma en
 * formato @firma.
 * 
 * @author miguel.moral
 *
 */
public class UtilFirma {

  private String policyIdentifierKey;
  private String signatureSubFilter;
  private String policyIdentifierHashKey;
  private String policyIdentifierHashAlgoritmKey;
  private String policyQualifierKey;
  private String policyDescriptionKey;
  private String policySignatureSubFilterKey;

  private String policySignatureSubFilterPades;
  private String policyIdentifierPades;
  private String policyIdentifierHashPades;
  private String policyIdentifierHashAlgorithmPades;
  private String policyQualifierPades;

  private String policyIdentifierXades;
  private String policyDescriptionXades;
  private String policyIdentifierHashXades;
  private String policyIdentifierHashAlgoritmXades;
  private String policyQualifierXades;

  private String policyIdentifierCades;
  private String policyIdentifierHashCades;
  private String policyIdentifierHashAlgoritmCades;
  private String policyQualifierCades;

  /**
   * Escribe las propiedades de firma traduciendo desde el formato especifico a policies.
   * 
   * @param formato El formato de la firma, puede ser: XAdES Detached, CAdES, Adobe PDF.
   * @param propiedadesFirma Las propiedades a las que se va a traducir el formato.
   * @return El formato procesado quitandole la cadena "EPES" si lo incluye y sustituyendolo por un
   *         espacio.
   * @throws EeutilException Excepcion generica de eeutils.
   */
  public String checkFormato(String formato, Properties propiedadesFirma) throws EeutilException {
    String retorno = formato;

    try {

      if (formato.equalsIgnoreCase(Constantes.FORMATO_XADES_DETACHED)) {
        propiedadesFirma.setProperty(policyIdentifierKey, policyIdentifierXades);
        propiedadesFirma.setProperty(policyIdentifierHashKey, policyIdentifierHashXades);
        propiedadesFirma.setProperty(policyIdentifierHashAlgoritmKey,
            policyIdentifierHashAlgoritmXades);
        propiedadesFirma.setProperty(policyQualifierKey, policyQualifierXades);
        propiedadesFirma.setProperty(policyDescriptionKey, policyDescriptionXades);
      }
      if (formato.contains(Constantes.FORMATO_CADES)) {
        propiedadesFirma.setProperty(policyIdentifierKey, policyIdentifierCades);
        propiedadesFirma.setProperty(policyIdentifierHashKey, policyIdentifierHashCades);
        propiedadesFirma.setProperty(policyIdentifierHashAlgoritmKey,
            policyIdentifierHashAlgoritmCades);
        propiedadesFirma.setProperty(policyQualifierKey, policyQualifierCades);
      }

      if (formato.contains(Constantes.FORMATO_ADOBE_PDF)) {
        propiedadesFirma.setProperty(policySignatureSubFilterKey, policySignatureSubFilterPades);
        propiedadesFirma.setProperty(policyIdentifierKey, policyIdentifierPades);
        propiedadesFirma.setProperty(policyIdentifierHashKey, policyIdentifierHashPades);
        propiedadesFirma.setProperty(policyIdentifierHashAlgoritmKey,
            policyIdentifierHashAlgorithmPades);
        propiedadesFirma.setProperty(policyQualifierKey, policyQualifierPades);
      }


      retorno = retorno.replaceFirst(" " + Constantes.FORMATO_EPES, "");

    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }

    return retorno;
  }

  /**
   * 
   * @return getPolicyIdentifierKey.
   */
  public String getPolicyIdentifierKey() {
    return policyIdentifierKey;
  }

  /**
   * 
   * @param policyIdentifierKey
   */
  public void setPolicyIdentifierKey(String policyIdentifierKey) {
    this.policyIdentifierKey = policyIdentifierKey;
  }

  /**
   * 
   * @return signatureSubFilter.
   */
  public String getSignatureSubFilter() {
    return signatureSubFilter;
  }

  /**
   * 
   * @param signatureSubFilter
   */
  public void setSignatureSubFilter(String signatureSubFilter) {
    this.signatureSubFilter = signatureSubFilter;
  }

  /**
   * 
   * @return policyIdentifierHashKey
   */
  public String getPolicyIdentifierHashKey() {
    return policyIdentifierHashKey;
  }

  /**
   * 
   * @param policyIdentifierHashKey
   */
  public void setPolicyIdentifierHashKey(String policyIdentifierHashKey) {
    this.policyIdentifierHashKey = policyIdentifierHashKey;
  }

  /**
   * 
   * @return policyIdentifierHashAlgoritmKey
   */
  public String getPolicyIdentifierHashAlgoritmKey() {
    return policyIdentifierHashAlgoritmKey;
  }

  /**
   * 
   * @param policyIdentifierHashAlgoritmKey
   */
  public void setPolicyIdentifierHashAlgoritmKey(String policyIdentifierHashAlgoritmKey) {
    this.policyIdentifierHashAlgoritmKey = policyIdentifierHashAlgoritmKey;
  }

  /**
   * 
   * @return policyQualifierKey
   */
  public String getPolicyQualifierKey() {
    return policyQualifierKey;
  }

  /**
   * 
   * @param policyQualifierKey
   */
  public void setPolicyQualifierKey(String policyQualifierKey) {
    this.policyQualifierKey = policyQualifierKey;
  }

  /**
   * 
   * @return policyIdentifierXades
   */
  public String getPolicyIdentifierXades() {
    return policyIdentifierXades;
  }

  /**
   * 
   * @param policyIdentifierXades
   */
  public void setPolicyIdentifierXades(String policyIdentifierXades) {
    this.policyIdentifierXades = policyIdentifierXades;
  }

  /**
   * 
   * @return policyDescriptionXades
   */
  public String getPolicyDescriptionXades() {
    return policyDescriptionXades;
  }

  /**
   * 
   * @param policyDescriptionXades
   */
  public void setPolicyDescriptionXades(String policyDescriptionXades) {
    this.policyDescriptionXades = policyDescriptionXades;
  }

  /**
   * 
   * @return policyIdentifierHashXades
   */
  public String getPolicyIdentifierHashXades() {
    return policyIdentifierHashXades;
  }

  /**
   * 
   * @param policyIdentifierHashXades
   */
  public void setPolicyIdentifierHashXades(String policyIdentifierHashXades) {
    this.policyIdentifierHashXades = policyIdentifierHashXades;
  }

  /**
   * 
   * @return policyIdentifierHashAlgoritmXades
   */
  public String getPolicyIdentifierHashAlgoritmXades() {
    return policyIdentifierHashAlgoritmXades;
  }

  /**
   * 
   * @param policyIdentifierHashAlgoritmXades
   */
  public void setPolicyIdentifierHashAlgoritmXades(String policyIdentifierHashAlgoritmXades) {
    this.policyIdentifierHashAlgoritmXades = policyIdentifierHashAlgoritmXades;
  }

  /**
   * 
   * @return policyQualifierXades
   */
  public String getPolicyQualifierXades() {
    return policyQualifierXades;
  }

  /**
   * 
   * @param policyQualifierXades
   */
  public void setPolicyQualifierXades(String policyQualifierXades) {
    this.policyQualifierXades = policyQualifierXades;
  }

  /**
   * 
   * @return policyIdentifierCades
   */
  public String getPolicyIdentifierCades() {
    return policyIdentifierCades;
  }

  /**
   * 
   * @param policyIdentifierCades
   */
  public void setPolicyIdentifierCades(String policyIdentifierCades) {
    this.policyIdentifierCades = policyIdentifierCades;
  }

  /**
   * 
   * @return policyIdentifierHashCades
   */
  public String getPolicyIdentifierHashCades() {
    return policyIdentifierHashCades;
  }

  /**
   * 
   * @param policyIdentifierHashCades
   */
  public void setPolicyIdentifierHashCades(String policyIdentifierHashCades) {
    this.policyIdentifierHashCades = policyIdentifierHashCades;
  }

  /**
   * 
   * @return policyIdentifierHashAlgoritmCades
   */
  public String getPolicyIdentifierHashAlgoritmCades() {
    return policyIdentifierHashAlgoritmCades;
  }

  /**
   * 
   * @param policyIdentifierHashAlgoritmCades
   */
  public void setPolicyIdentifierHashAlgoritmCades(String policyIdentifierHashAlgoritmCades) {
    this.policyIdentifierHashAlgoritmCades = policyIdentifierHashAlgoritmCades;
  }

  /**
   * 
   * @return policyQualifierCades
   */
  public String getPolicyQualifierCades() {
    return policyQualifierCades;
  }

  /**
   * 
   * @param policyQualifierCades
   */
  public void setPolicyQualifierCades(String policyQualifierCades) {
    this.policyQualifierCades = policyQualifierCades;
  }

  /**
   * 
   * @return policyDescriptionKey
   */
  public String getPolicyDescriptionKey() {
    return policyDescriptionKey;
  }

  /**
   * 
   * @param policyDescriptionKey
   */
  public void setPolicyDescriptionKey(String policyDescriptionKey) {
    this.policyDescriptionKey = policyDescriptionKey;
  }

  /**
   * 
   * @return policySignatureSubFilterKey
   */
  public String getPolicySignatureSubFilterKey() {
    return policySignatureSubFilterKey;
  }

  /**
   * 
   * @param policySignatureSubFilterKey
   */
  public void setPolicySignatureSubFilterKey(String policySignatureSubFilterKey) {
    this.policySignatureSubFilterKey = policySignatureSubFilterKey;
  }

  /**
   * 
   * @return policySignatureSubFilterPades
   */
  public String getPolicySignatureSubFilterPades() {
    return policySignatureSubFilterPades;
  }

  /**
   * 
   * @param policySignatureSubFilterPades
   */
  public void setPolicySignatureSubFilterPades(String policySignatureSubFilterPades) {
    this.policySignatureSubFilterPades = policySignatureSubFilterPades;
  }

  /**
   * 
   * @return policyIdentifierPades
   */
  public String getPolicyIdentifierPades() {
    return policyIdentifierPades;
  }

  /**
   * 
   * @param policyIdentifierPades
   */
  public void setPolicyIdentifierPades(String policyIdentifierPades) {
    this.policyIdentifierPades = policyIdentifierPades;
  }

  /**
   * 
   * @return policyIdentifierHashPades
   */
  public String getPolicyIdentifierHashPades() {
    return policyIdentifierHashPades;
  }

  /**
   * 
   * @param policyIdentifierHashPades
   */
  public void setPolicyIdentifierHashPades(String policyIdentifierHashPades) {
    this.policyIdentifierHashPades = policyIdentifierHashPades;
  }

  /**
   * 
   * @return policyIdentifierHashAlgorithmPades
   */
  public String getPolicyIdentifierHashAlgorithmPades() {
    return policyIdentifierHashAlgorithmPades;
  }

  /**
   * 
   * @param policyIdentifierHashAlgorithmPades
   */
  public void setPolicyIdentifierHashAlgorithmPades(String policyIdentifierHashAlgorithmPades) {
    this.policyIdentifierHashAlgorithmPades = policyIdentifierHashAlgorithmPades;
  }

  /**
   * 
   * @return policyQualifierPades
   */
  public String getPolicyQualifierPades() {
    return policyQualifierPades;
  }

  /**
   * 
   * @param policyQualifierPades
   */
  public void setPolicyQualifierPades(String policyQualifierPades) {
    this.policyQualifierPades = policyQualifierPades;
  }

}
