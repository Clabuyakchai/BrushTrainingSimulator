package com.example.clabuyakchai.brushtrainingsimulator.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Clabuyakchai on 09.05.2018.
 */

public class SOAPRequest {
    //TODO
    private static final String NAMESPACE = "http://com.clabuyakchai/soap";
    private static final String URL="http://192.168.5.104:8090/ws/info.wsdl";
    private static final String METHOD_NAME = "getInfoRequest";
    private static final String SOAP_ACTION = "http://com.clabuyakchai/soap/getInfoRequest";

    public static String requestSoap(){

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo info = new PropertyInfo();
        info.setName("info_project");
        info.setType(String.class);
        request.addProperty(info);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.debug = true;
            httpTransportSE.call(SOAP_ACTION, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return response.toString();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
