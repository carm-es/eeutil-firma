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

/**
 * Excepcion asociada a la firma de eeutils.
 * 
 * @author miguel.moral
 *
 */
public class MPTFirmaException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final String ERROR_DESCONOCIDO = "Error desconocido";
  private static final String ERROR_NO_CONTROLADO_POR_LA_APLICACION =
      "Error no controlado por la aplicacion.";


  /**
   * Constructor de la clase.
   * 
   * @param message mensaje.
   * @param cause excepcion.
   */
  public MPTFirmaException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor de la clase.
   * 
   * @param message mensaje.
   */
  public MPTFirmaException(String message) {
    super(message);
  }

  /**
   * Constructor de la clase.
   */
  public MPTFirmaException() {
    super(ERROR_NO_CONTROLADO_POR_LA_APLICACION);
  }

  /**
   * Constructor de la clase.
   * 
   * @param cause excepcion.
   */
  public MPTFirmaException(Throwable cause) {
    super(ERROR_NO_CONTROLADO_POR_LA_APLICACION + cause.getMessage());
  }

  /**
   * Tipo de error.
   * 
   * @return devuele tipo de error desconocido.
   */
  public String getTipoError() {
    return ERROR_DESCONOCIDO;
  }
}

