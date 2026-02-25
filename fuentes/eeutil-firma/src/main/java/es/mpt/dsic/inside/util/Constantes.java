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

package es.mpt.dsic.inside.util;

/**
 * Constantes asociadas al servicio eeutil-firma
 * 
 * @author miguel.moral
 *
 */
public class Constantes {

  private Constantes() {

  }

  /**
   * clave de la propiedad para definir el formato de firma por defecto, viene de bbdd y se define
   * para cada aplicacion.
   */
  public static final String FORMATO_FIRMA_DEFECTO_PROP_BBDD = "formatoFirmaDefecto";
  /**
   * clave de la propiedad para definir el modo de firma por defecto, viene de bbdd y se define para
   * cada aplicacion.
   */
  public static final String MODO_FIRMA_DEFECTO_PROP_BBDD = "modoFirmaDefecto";
  /**
   * clave de la propiedad para definir el algoritmo de firma por defecto, viene de bbdd y se define
   * para cada aplicacion.
   */
  public static final String ALGORITMO_FIRMA_DEFECTO_PROP_BBDD = "algoritmoFirmaDefecto";
  /**
   * clave de la propiedad para definir la ruta del almacen de certificados, viene de bbdd y se
   * define para cada aplicacion.
   */
  public static final String RUTA_ALMACEN_CERTIFICADO_PROP_BBDD = "rutaKS";
  /**
   * clave de la propiedad para definir el password del almacen de certificados, viene de bbdd y se
   * define para cada aplicacion.
   */
  public static final String PASS_ALMACEN_CERTIFICADO_PROP_BBDD = "passwordKS";
  /**
   * clave de la propiedad para definir el alias de certificado en uso para firmar, viene de bbdd y
   * se define para cada aplicacion.
   */
  public static final String ALIAS_CERTIFICADO_PROP_BBDD = "aliasCertificado";
  /**
   * clave de la propiedad para definir el password de certificado en uso para firmar, viene de bbdd
   * y se define para cada aplicacion.
   */
  public static final String PASS_CERTIFICADO_PROP_BBDD = "passwordCertificado";
  /**
   * clave de la propiedad para definir el tipo de almacen de certificado, viene de bbdd y se define
   * para cada aplicacion.
   */
  public static final String TIPO_ALMACEN_CERTIFICADO_PROP_BBDD = "tipoKS";
  /**
   * Definicion de error formato firma incorrecto.
   */
  public static final String FORMATO_FIRMA_INCORRECTO_PROP = "FORMATO_FIRMA_INCORRECTO";
  /**
   * Definicion de error en el proceso de firma.
   */
  public static final String ERROR_PROCESO_FIRMA_PROP = "ERROR_PROCESO_FIRMA";
  /**
   * Definicion de error de datos incorrectos.
   */
  public static final String DATOS_INCORRECTOS_PROP = "DATOS_INCORRECTOS";
  /**
   * Definicion de error de certificado no encontrado.
   */
  public static final String CERT_NO_ENCONTRADO_PROP = "CERT_NO_ENCONTRADO";
  /**
   * Definicion de error de certificado no abierto.
   */
  public static final String CERT_NO_ABIERTO_PROP = "CERT_NO_ABIERTO";
  /**
   * Definicion de error de pk no abierto.
   */
  public static final String PK_NO_ABIERTO_PROP = "PK_NO_ABIERTO";
  /**
   * Definicion de error algoritmo de huella no valida.
   */
  public static final String ALGORITMO_HUELLA_NO_VALIDA = "ALGORITMO_HUELLA_NO_VALIDA";
  /**
   * Definicion de error de huella no valida.
   */
  public static final String HUELLA_NO_VALIDA = "HUELLA_NO_VALIDA";


  /**
   * Clave de operacion.
   */
  public static final String OPERACION_FIRMA = "FIRMA";
  /**
   * Clave de operacion.
   */
  public static final String OPERACION_COFIRMA = "COFIRMA";
  /**
   * Clave de operacion.
   */
  public static final String OPERACION_CONTRAFIRMAR_HOJAS = "CONTRAFIRMA_HOJAS";
  /**
   * Clave de operacion.
   */
  public static final String OPERACION_CONTRAFIRMAR_ARBOL = "CONTRAFIRMA_ARBOL";
  /**
   * Formato de firma EPES.
   */
  public static final String FORMATO_EPES = "EPES";
  /**
   * Formato de firma XAdES Detached.
   */
  public static final String FORMATO_XADES_DETACHED = "XAdES Detached";
  /**
   * Formato de firma CAdES.
   */
  public static final String FORMATO_CADES = "CAdES";
  /**
   * Formato de firma Adobe PDF.
   */
  public static final String FORMATO_ADOBE_PDF = "Adobe PDF";
}
