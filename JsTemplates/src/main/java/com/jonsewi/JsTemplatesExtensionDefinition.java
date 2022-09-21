package com.jonsewi;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class JsTemplatesExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("b2c4be26-dc3b-470c-8f3a-45dac45e99b8");
   
   public JsTemplatesExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "JsTemplates";
   }
   
   @Override
   public String getAuthor()
   {
      return "JonSewi";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "JonSewi";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "JsTemplates";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 17;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 0;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public JsTemplatesExtension createInstance(final ControllerHost host)
   {
      return new JsTemplatesExtension(this, host);
   }
}
