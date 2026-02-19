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

package es.mpt.dsic.inside.util.firma;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mpt.dsic.inside.config.EeutilApplicationDataConfig;
import es.mpt.dsic.inside.config.EeutilConfigPath;
import es.mpt.dsic.inside.utils.exception.EeutilException;

/**
 * Clase para manejo de properties.
 * 
 * @author miguel.moral
 *
 */
public class TextosErroresFirma {

  private static final String APPLICATION_NAME = "application.name";
  private static final String APPLICATION_PROPERTIES = "application.properties";

  private static TextosErroresFirma instance = null;
  private Properties propiedades = null;

  protected static final Log logger = LogFactory.getLog(TextosErroresFirma.class);

  /**
   * Obtiene la instancia de la clase,
   * 
   * @return devuelve la instancia.
   * @throws EeutilException la excepcion.
   */
  public static TextosErroresFirma getInstance() throws EeutilException {

    try {

      if (instance == null) {
        instance = new TextosErroresFirma();
      }
    }

    catch (EeutilException e) {
      throw new EeutilException(
          "No se ha podido obtener la instancia de TextosErroresFirma " + e.getMessage(), e);
    }

    return instance;
  }

  /**
   * 
   * @throws EeutilException
   */
  private TextosErroresFirma() throws EeutilException {
    try {
      propiedades = new Properties();
      Properties props = EeutilApplicationDataConfig.loadProperties(APPLICATION_PROPERTIES);

      /**
       * String ruta = System.getProperty("config.path") + "/textos.errores.firma.properties";
       */
      String ruta = null;
      if (System.getenv(
          props.getProperty(APPLICATION_NAME) + "." + EeutilConfigPath.CONFIG_PATH_VAR) != null) {
        ruta = System
            .getenv(props.getProperty(APPLICATION_NAME) + "." + EeutilConfigPath.CONFIG_PATH_VAR)
            + "/textos.errores.firma.properties";
      } else {
        ruta = System.getProperty(
            props.getProperty(APPLICATION_NAME) + "." + EeutilConfigPath.CONFIG_PATH_VAR)
            + "/textos.errores.firma.properties";
      }

      try (FileInputStream fin = new FileInputStream(ruta);) {
        propiedades.load(fin);
      }
    } catch (IOException e) {
      throw new EeutilException("Error al obtener los mensajes de firma " + e.getMessage(), e);
    }

  }

  /**
   * Obtiene la propiedad del fichero properties.
   * 
   * @param key clave de busqueda.
   * @return el valor.
   */
  public String getProperty(String key) {
    return propiedades.getProperty(key);
  }

}
