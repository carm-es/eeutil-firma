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

package es.mpt.dsic.inside.ws.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.mpt.dsic.inside.exception.base.DataWsEntradaBase;
import es.mpt.dsic.inside.exception.interfaz.IMDCAble;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datosEntradaFichero", propOrder = {"contenido", "formatoFirma", "modoFirma",
    "algoritmoFirma", "cofirmarSiFirmado", "nodeToSign"})
public class DatosEntradaFichero extends DataWsEntradaBase implements IMDCAble {

  private static final String DEFAULT_FORMATO_FIRMA = "CAdES";
  private static final String DEFAULT_MODO_FIRMA = "implicit";
  private static final String DEFAULT_ALGORITMO_FIRMA = "SHA256withRSA";


  @XmlElement(required = true, name = "contenido")
  private byte[] contenido;

  @XmlElement(required = false, name = "formatoFirma")
  private String formatoFirma;
  @XmlElement(required = false, name = "modoFirma")
  private String modoFirma;
  @XmlElement(required = false, name = "algoritmoFirma")
  private String algoritmoFirma;
  @XmlElement(required = false, name = "cofirmarSiFirmado")
  private boolean cofirmarSiFirmado;
  @XmlElement(required = false, name = "nodeToSign")
  private String nodeToSign;

  public byte[] getContenido() {
    return contenido;
  }

  public void setContenido(byte[] contenido) {
    this.contenido = contenido;
  }

  public String getFormatoFirma() {
    return formatoFirma;
  }

  public void setFormatoFirma(String formatoFirma) {
    if (formatoFirma != null && !formatoFirma.isEmpty()) {
      this.formatoFirma = formatoFirma;
    } else {
      this.formatoFirma = DEFAULT_FORMATO_FIRMA;
    }
  }

  public String getModoFirma() {
    return modoFirma;
  }

  public void setModoFirma(String modoFirma) {
    if (modoFirma != null && !modoFirma.isEmpty()) {
      this.modoFirma = modoFirma;
    } else {
      this.modoFirma = DEFAULT_MODO_FIRMA;
    }
  }

  public String getAlgoritmoFirma() {
    return algoritmoFirma;
  }

  public void setAlgoritmoFirma(String algoritmoFirma) {
    if (algoritmoFirma != null && !algoritmoFirma.isEmpty())
      this.algoritmoFirma = algoritmoFirma;
    else
      this.algoritmoFirma = DEFAULT_ALGORITMO_FIRMA;
  }

  public boolean isCofirmarSiFirmado() {
    return cofirmarSiFirmado;
  }

  public void setCofirmarSiFirmado(boolean cofirmarSiFirmado) {
    this.cofirmarSiFirmado = cofirmarSiFirmado;
  }



  public String getNodeToSign() {
    return nodeToSign;
  }

  public void setNodeToSign(String nodeToSign) {
    this.nodeToSign = nodeToSign;
  }


}

