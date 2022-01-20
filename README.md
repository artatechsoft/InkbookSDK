# inkBOOK software adaptation guide



#### Powered by  [![N|Solid](logo_inkBOOK.jpg)](https://www.inkbook.eu/)

## Preface
This document gathers all information useful for adopting Android applications to be used on inkBOOK  E Ink devices.


## User Interface simplifications
### Widget background 

Dark backgrounds and colorful gradients should be avoided.  When graphic is scaled to 16-level grayscale this may lead to unreadable text sections or even in most drastic scenarios to displaying back text on black background. It is highly recommended to follow guidelines form Recommended colour range section.
### Animation effects
All pageturn and other animation effects should be removed. Rich animated interface will not be displayed properly in E Ink environment. This may lead to unpleasant screen flickering, random erratic screen refreshments or at least odd behaviour of interface components.
### Item lists - scrolling vs pagination
inkBOOK Android system is customised for E Ink and can handle scroll events animation effects. However using scrolled lists in interface and especially scrolled content in activity windows is not recommended. In order to display scroll animation properly screen needs to change display mode form 16-level grayscale to black and white only and then go back to 16-level grayscale again when animation ends. Interfaces and content converted to 2 colours (black and white) does not look well, looses all of details and changing screen mode requires full screen refresh each time, which results in unpleasant screen flickering. 
It is highly recommended to use list pagination instead of scrolling as often as possible.

We have made avaiable custom paging recycler view, that can be used to replace Android RecyclerView.
The view and information of how to use it can be found:

https://github.com/artatechsoft/InkbookSDK/tree/master/inkbook-pagingrecyclerview

Compiled library

https://github.com/artatechsoft/InkbookSDK/tree/master/inkbook-pagingrecyclerview-library



### Recommended colour range
Due to nature of E Ink screen all colours are mapped to 16-level grayscale. In order to keep clean and sleek user interface we recommend using high contrasting colour scheme:


| Usage                       | Colour  |
|-----------------------------|---------|
| Background                  | #ffffff |
| Text                        | #000000 |
| Alternative Background      | #dadada |
| Reverse notation Background | #000000 |
|  Reverse notation Text      | #ffffff |
### Images
Because of nature of E Ink screen sometimes scaled images might appear more blurry than the same images used as tablet/smartphone UI elements. It is highly recommended to perform visual verification of all image UI elements and icons prior to application release. If some of graphic are blurry, we recommend to prepare them in exact required resolution in order to avoid software scaling down or scaling up. Current screen parameters are:

| Model             | Resolution  | Pixel Density |
|-------------------|-------------|---------------|
| inkBOOK Calypso plus | 1024x758 px  | 212 dpi       |
| inkBOOK Focus     | 1872x1404 px | 300 dpi       |
 
## Power management features
### Background synchronization
In order to keep power usage optimal all background data processing should be limited to absolute minimum to avoid high CPU loads and allow system to enter Idle state as often as possible.
Additionally WiFi connectivity should never be used for extended period of time and especially it’s not recommended to keep WiFi connection up artificially (WiFi automatically powers down after few minutes of inactivity). During WiFi data transfer inkBOOK power usage might increase up to 10 times of normal battery consumption. As result extensive WiFi usage may reduce weeks of battery lifetime to merely days. 
### Idle mode
The significant difference between reading apps designed to be used on smartphone/tablet and e-reader is how the Idle mode is handled. Devices using standard LCD screens tend to dim or power down screen when entering Idle mode. This is unwanted behaviour, so Idle mode is often being disabled in reader app. E-reader screen does not require any power to keep it’s current state, so this problem does not exist at all.
In order to keep power usage optimal it is highly recommended to remove or disable all code preventing Android from entering Idle mode. Example:
```java
myWakeLock.acquire();  // prevent Idle
...
myWakeLock.release();  // allow Idle
```
Neglecting proper Idle mode management may lead to increase of battery usage up to 3 times more than normal.
### Device identification
It is understandable that developing separate Android application just for e-reader usage might not be the best business approach. However, it is very easy to identify inkBOOK e-reader and relay on this information to diversify crucial parts of code and user interfaces.

To properly identify inkBOOK e-reader:

    InkBookSDK.isInkBook()

### Key mapping
InkBOOK is equipped with 5 buttons in total (excluding Power button). Four of those buttons are fully programmable and user may assign actions to them freely.

| Action                     | Value                        |
|----------------------------|------------------------------|
| Page Up                    | KeyEvent.KEYCODE_PAGE_UP     |
| Page Down                  | KeyEvent.KEYCODE_PAGE_DOWN   |
| Back                       | KeyEvent.KEYCODE_BACK        |
| Volume Up                  | KeyEvent.KEYCODE_VOLUME_UP   |
| Volume Down                | KeyEvent.KEYCODE_VOLUME_DOWN |
| Disable/Enable Touch Panel | KeyEvent.KEYCODE_CAMERA      |
| Display Menu               | KeyEvent.KEYCODE_MENU        |

```java
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {

    switch (keyCode) {
        case KeyEvent.KEYCODE_PAGE_UP:
        {
            //your Action code
            return true;
        }
    }
    return super.onKeyDown(keyCode, event);
}
```


### How to properly refresh screen
In order to avoid unwanted so called ghosting effect (when part of previous screen context remains as a shadow on the screen), it is necessary to perform full screen refresh from time to time. Full screen refresh causes black and white blink on screen and uses more power when normal partial refresh, so it should be used as seldom as possible. User defines prefered fullscreen refreshment interval in device settings quantified as number of page turns until full screen refresh.
This value could be accessed by calling code:

    InkBookSDK.getPagesToRefresh(_Activity_)

Then to invoke full screen refresh:

	InkBookSDK.refreshScreen(_context_)

inkBOOK devices are using advanced E Ink driver that can reduce ghosting effect significantly and improve screen performance (Rapid Refresh), but it is less efficient with non-vector (generally not text) content. That’s why two separate screen refresh modes were implemented:
Rapid Refresh Mode - recommended for text viewing,

    InkBookSDK.setRAPIDMode(_context_)

Part Refresh Mode - recommended for all other activities.
	
	InkBookSDK.setPARTMode(_context_)

In order to achieve the best performance Rapid Refresh mode should be used for browsing text. However, any other activity like opening any kind of menu, closing book, etc. should be followed by switching back to Part Refresh Mode. Forcing system to work in Rapid Refresh Mode may cause issues with displaying Android UI components. For example: partial ghosting effects on UI icons, smudges on animations. 

## Optional integrations
### Main screen widget
In some cases, when private DRM solution is being used or for other reasons content provided by service could not be accessed and properly indexed by inkBOOK e-book scanner, it might be necessary to customize widgets presented on Main Screen. In order to provide seamless experience to end user we recommend to create custom widget displaying data from the application (such as recent books, currently read book, new offers etc.) provided together with main application.

Available widget space: **457x530 dp**


### Translator support
inkBOOK introduces translation services based on Google Translate. Only the users that have accepted the GoogleTranslateLicence are able to use translator. The method below isGoogleTranslateInstalled(Context context) can be used to check wheater Translator support is enabled.


```java
    public static boolean isGoogleTranslateInstalled(Context context)
    {
         final String GOOGLE_TRANSLATE_PACKAGE = "com.google.android.apps.translate";
         final String ARTATECH_GOOGLE_TRANSLATE_VERSION = "5.4.0.RC10.132942120";

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(GOOGLE_TRANSLATE_PACKAGE, 0);
            String version = pInfo.versionName;
            return version.equals(ARTATECH_GOOGLE_TRANSLATE_VERSION);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
```

In order to diplay trnaslator popup window intent below can be used, with extra variable text containg String that should be translated.

```java
     Intent intent = new  Intent();
                intent.setAction(Intent.ACTION_PROCESS_TEXT);
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, text);

                intent.setComponent( new ComponentName(
                        "com.google.android.apps.translate",
                        "com.google.android.apps.translate.copydrop.CopyDropActivity"));
                context.startActivity(intent);

context.startActivity(intent);

```

# InkBookSDK

## InkBookSDK usages ( InkBookSdk library works only with compatible devices, listed below)
  - **InkBookSDK.isEInk(_Activity_)** ---> true if current device use E-Ink display
  - **InkBookSDK.isInkBook()** ---> true if device is InkBook
  - **InkBookSDK.refreshScreen(__Activity__)** ---> refresh device screen
  - **InkBookSDK.refreshView(__View__)** ---> refresh device screen
  - **InkBookSDK.setA2Mode(__Activity__)** ---> switch display to A2 mode
  - **InkBookSDK.a2View(__View__)** ---> switch view to A2 mode
  - **InkBookSDK.refresh(__Activity__)** ---> refresh device screen
  - **InkBookSDK.refreshScreen(__Activity__, __Int__)** ---> refresh device screen by introduced mode
  - **InkBookSDK.refreshView(__View__, __Int__)** ---> refresh view by introduced mode
  - **InkBookSDK.refresh(__Activity__, __Int__)** ---> refresh device screen by introduced mode
  - **InkBookSDK.getCurrentRefreshMode()** ---> return name of current refresh mode (RAPID,PART,A2)
  - **InkBookSDK.getBrightness(__Activity__)** --> return current value of brightness
  - **InkBookSDK.getTemperature(__Activity__)** --> return current value of temperature
  - **InkBookSDK.setBrightness(__Activity__, __Int__)** --> change brightness to introduced value (max 255)
  - **InkBookSDK.setTemperature(__Activity__, __Int__)** --> change temperature to introduced value (max 255)

Chromium methods are unsupported for Focus and Calypso Plus devices.

### InkBookSDK available modes

```java
class EInkRefreshUtil {
public static int EPD_NULL=-1;
public static int EPD_FULL=1;
public static int EPD_A2=2;
}
```

EPD_NULL: Invalid mode.  
EPD_FULL: Full refresh mode, completely use GRAY16 waveform data to refresh the screen. This mode has the slowest refresh speed, with less residue but it will flicker. can be used to periodically clear the residue caused by the refresh of other modes.  
EPD_A2: A2 mode, generally used for video, picture and other scenarios. Comparing the data of before and after frames, the changed pixels use A2 waveform data, and the waveform data of the unchanged pixels are 0. Only support the refresh of black and white. When the application selects this mode, the system will automatically refresh with DU mode before refresh with A2 mode, in order to adapt with the change from 16 grey scale to 2 grey scale. Besides, this mode has dither algorithm. This mode has fast refresh speed.  




LIST OF COMPATIBLE DEVICES:
1. inkBOOK Calypso plus 6"
2. inkBOOK Focus 7,8"
