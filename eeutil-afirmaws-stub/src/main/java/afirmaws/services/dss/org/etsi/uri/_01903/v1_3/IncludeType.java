/*
 * Copyright (C) 2012-13 MINHAP, Gobierno de España This program is licensed and may be used,
 * modified and redistributed under the terms of the European Public License (EUPL), either version
 * 1.1 or (at your option) any later version as soon as they are approved by the European
 * Commission. Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * more details. You should have received a copy of the EUPL1.1 license along with this program; if
 * not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */


package afirmaws.services.dss.org.etsi.uri._01903.v1_3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for IncludeType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IncludeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="URI" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="referencedData" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncludeType")
public class IncludeType {

  @XmlAttribute(name = "URI", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String uri;
  @XmlAttribute(name = "referencedData")
  protected Boolean referencedData;

  /**
   * Gets the value of the uri property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getURI() {
    return uri;
  }

  /**
   * Sets the value of the uri property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setURI(String value) {
    this.uri = value;
  }

  /**
   * Gets the value of the referencedData property.
   * 
   * @return possible object is {@link Boolean }
   * 
   */
  public Boolean isReferencedData() {
    return referencedData;
  }

  /**
   * Sets the value of the referencedData property.
   * 
   * @param value allowed object is {@link Boolean }
   * 
   */
  public void setReferencedData(Boolean value) {
    this.referencedData = value;
  }

}
