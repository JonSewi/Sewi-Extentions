package com.jonsewi;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class TrackTemplatesExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("d6edc82e-963f-4042-8ec3-5a2a74d39062");
   
   public TrackTemplatesExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "TrackTemplates";
   }
   
   @Override
   public String getAuthor()
   {
      return "Jwaard";
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
      return "Generic";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "TrackTemplates";
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
   public TrackTemplatesExtension createInstance(final ControllerHost host)
   {
      return new TrackTemplatesExtension(this, host);
   }
}
