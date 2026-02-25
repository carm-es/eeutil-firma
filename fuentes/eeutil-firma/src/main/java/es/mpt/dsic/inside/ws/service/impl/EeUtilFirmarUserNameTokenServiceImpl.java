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

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditEntryPointAnnotation;
import es.mpt.dsic.inside.ws.service.EeUtilFirmarUserNameTokenService;
import es.mpt.dsic.inside.ws.service.model.DatosEntradaFichero;
import es.mpt.dsic.inside.ws.service.model.DatosSalida;

/**
 * Implementacion de ws usernametoken de eeutil-firma.
 * 
 * @author miguel.moral
 *
 */
@Service("eeUtilFirmarUserNameTokenService")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilFirmarUserNameTokenService")
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.BARE, use = Use.LITERAL)
public class EeUtilFirmarUserNameTokenServiceImpl implements EeUtilFirmarUserNameTokenService {


  @Autowired
  EeUtilFirmarServiceImplBusiness eeUtilFirmarServiceImplBusiness;

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-FIRMA")
  public DatosSalida firmaFichero(DatosEntradaFichero entrada) {
    return eeUtilFirmarServiceImplBusiness.firmaFichero(entrada);

  }

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-FIRMA")
  public DatosSalida firmaFicheroWithPropertie(DatosEntradaFichero entrada) {

    return eeUtilFirmarServiceImplBusiness.firmaFicheroWithPropertie(entrada);

  }

}
