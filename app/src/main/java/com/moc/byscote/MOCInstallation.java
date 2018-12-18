package com.moc.byscote;

import android.content.Context;
import android.provider.Settings.Secure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

//import java.io.UnsupportedEncodingException;
//import android.telephony.TelephonyManager;

// Using Interal Storeage (private, can only accessed by this app)

public class MOCInstallation {
    private static final String MOC_UUID_FILE = "MOC_UUID";
    private static String sUUID = null;
    private static final String MOC_ANDROID_ID_FILE = "MOC_ANDROID_ID";
    private static String sAndroidID = null;

    public synchronized static String UUID(Context context) {
        if (sUUID == null) {  
            File afile = new File(context.getFilesDir(), MOC_UUID_FILE);
            try {
                if (!afile.exists()) {
                    String id = UUID.randomUUID().toString();
                    writeFile(afile, id);
                }
                sUUID = readFile(afile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sUUID;
    }

    public synchronized static String androidID(Context context) {
        if (sAndroidID == null) {  
            File afile = new File(context.getFilesDir(), MOC_ANDROID_ID_FILE);
            try {
                if (!afile.exists()) {
                    String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                    //UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                	/*
                    try {
                        if ("9774d56d682e549c".equals(androidId)) {
                            final String deviceId = (
                                (TelephonyManager) context
                                .getSystemService(Context.TELEPHONY_SERVICE))
                                .getDeviceId();
                            uuid = deviceId != null 
                            	? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) 
                            	: UUID.randomUUID();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }                    
                    */
                    //String id = uuid.toString();
                    writeFile(afile, androidId);
                }
                sAndroidID = readFile(afile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    	return sAndroidID;
    }

    private static String readFile(File afile) throws IOException {
        RandomAccessFile f = new RandomAccessFile(afile, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeFile(File afile, String str) throws IOException {
        FileOutputStream out = new FileOutputStream(afile);
        out.write(str.getBytes());
        out.close();
    }
}
/*
// Using Shared Preferences (can be seen by user and other apps)
public class MOCInstallation {
	private static String uniqueID = null;
	private static final String PREF_UNIQUE_ID = "MYANMAR";

	public synchronized static String id(Context context) {
		if (uniqueID == null) {
			SharedPreferences sharedPrefs = context.getSharedPreferences(
					PREF_UNIQUE_ID, Context.MODE_PRIVATE);
			uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
			if (uniqueID == null) {
				uniqueID = UUID.randomUUID().toString();
				Editor editor = sharedPrefs.edit();
				editor.putString(PREF_UNIQUE_ID, uniqueID);
				editor.commit();
			}
		}
		return uniqueID;
	}
}
*/