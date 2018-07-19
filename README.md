# inkBOOK software adaptation guide



#### Powered by  [![N|Solid](https://static.wixstatic.com/media/d62b24_e50f96dd7eed455fa9ce2782af77c431.png/v1/fill/w_157,h_96,al_c,usm_0.66_1.00_0.01/d62b24_e50f96dd7eed455fa9ce2782af77c431.png)](https://www.artatech.pl/)

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
| inkBOOK Classic 2 | 600x800 px  | 167 dpi       |
| inkBOOK Prime     | 768x1024 px | 212 dpi       |
 
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

### How to properly refresh screen
In order to avoid unwanted so called ghosting effect (when part of previous screen context remains as a shadow on the screen), it is necessary to perform full screen refresh from time to time. Full screen refresh causes black and white blink on screen and uses more power when normal partial refresh, so it should be used as seldom as possible. User defines prefered fullscreen refreshment interval in device settings quantified as number of page turns until full screen refresh.
This value could be accessed by calling code:

    InkBookSDK.getPagesToRefresh(_Activity_)

Then to invoke full screen refresh:

	InkBookSDK.refreshScreen(_context_)

inkBOOK Prime is using advanced E Ink driver that can reduce ghosting effect significantly and improve screen performance (Rapid Refresh), but it is less efficient with non-vector (generally not text) content. That’s why two separate screen refresh modes were implemented:
Rapid Refresh Mode - recommended for text viewing,

    InkBookSDK.setRAPIDMode(_context_)

Part Refresh Mode - recommended for all other activities.
	
	InkBookSDK.setPARTMode(_context_)

In order to achieve the best performance Rapid Refresh mode should be used for browsing text. However, any other activity like opening any kind of menu, closing book, etc. should be followed by switching back to Part Refresh Mode. Forcing system to work in Rapid Refresh Mode may cause issues with displaying Android UI components. For example: partial ghosting effects on UI icons, smudges on animations. 

## Optional integrations
### Main screen widget
In some cases, when private DRM solution is being used or for other reasons content provided by service could not be accessed and properly indexed by inkBOOK e-book scanner, it might be necessary to customize widgets presented on Main Screen. In order to provide seamless experience to end user we recommend to create custom widget displaying data from the application (such as recent books, currently read book, new offers etc.) provided together with main application.

Available widget space: **457x530 dp**

### Dictionary support
inkBOOK introduces dictionary service based on MediaWiki Wiktionary project dump files, that are converted to local database and prepared for offline usage. Dictionary service is not necessary integration and it remains purely optional. However,  access to dictionaries directly from reader application is experienced highly appreciated by e-reader users. That’s why inkBOOK dictionary service is opened to be used even in third party applications.
It is possible to search phrase directly in dictionary application. 

```java
Intent intent = new Intent()
.setComponent(new ComponentName("com.artatech.inkbook.inbookdictionary",  "com.artatech.inkbook.inbookdictionary.DictSearch.MainActivity"))
.putExtra("search", "query")
.putExtra("search_lang", "lang")
.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

context.startActivity(intent);

```

Usage of dictionary widget described in section **Dictionary widget integration**.
# InkBookSDK

## inkBOOK specific features
###### InkBookSDK allows not only use InkBook features also to integrate Dictionary SDK with yours applications
##### IMPORTANT:
**To Integrate DictionarySDK device must have InkBookDictionary application installed**


### Dictionary widget integration
   - install InkBookDictionary application (if not preinstalled)
   - download [inkbooksdk.aar](https://github.com/artatechsoft/InkbookSDK/blob/master/inkbooksdk.aar)
   - put the aar file in your libs directory and add a directory repository in _**build.gradle**_ file:
```java
   repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile(name: 'inkbooksdk', ext: 'aar')
}
```

   - Sync project with gradle


#### Dictionary widget usage

   - Initialize DictSDK with Application class and add this class to Manifest:

```java
   public class BuilderApplication extends Application {
     @Override
        public void onCreate() {
            super.onCreate();
            DictSDK.init(this,true);
        }
    }
```

```java
 <application
        android:name=".BuilderApplication"
```

   - Create and show widget
```java
        DictViewBuilder widget = new DictViewBuilder(getActivity()) //creating new instance with context
                .search("query") // set query to find in dictionary
                .setDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getActivity(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                }) // create onDismissListener (optional)
                .setContainer((ViewGroup) getView().findViewById(R.id.container)); //set container View to show widget

        widget.show(); // show widget in container View
```

You can also dismiss widget:
```java
widget.dismiss();
```

#### Add ChromView support to the application

To set default rendering engine to ChromeView you need to run method in Application class:

```java
public class YourApp extends Application {
...
    @Override
    public void onCreate() {
        super.onCreate();
        InkBookSDK.addChromiumSupport(this);
	...
    }
```

#### InkBookSDK usages
  - **InkBookSDK.isEInk(_Activity_)** ---> true if current device use E-Ink display
  - **InkBookSDK.isInkBook()** ---> true if device is InkBook (_InkBook Prime or InkBook Classic2_)
  - **InkBookSDK.isFullSupported()** ---> device can switch between PART and RAPID mode (_InkBook Prime_)
  - **InkBookSDK.isRefreshSupport()** ---> device can only refresh screen (_InkBook Classic2_)
  - **InkBookSDK.addChromiumSupport(_Application_)** ---> sets Chromium as default rendering engine for applicartion.
  - **InkBookSDK.isChromiumUsed(_Activity_)** ---> return true if current WebView provider is Chromium
  - **InkBookSDK.getChromiumVersion(_Activity_)** ---> return version of Chromium used in system or "WebViewClassic" for old webview
  - **InkBookSDK.getCurrentRefreshMode()** ---> return name of current refresh mode (RAPID,PART,A2)
  - **InkBookSDK.setRAPIDMode(_Activity_)** ---> switch display to RAPID mode
  - **InkBookSDK.setPARTMode(_Activity_)** ---> switch display to PART mode
  - **InkBookSDK.setA2Mode(_Activity_)** ---> switch display to A2 mode
  - **InkBookSDK.disableWebViewAutoRefresh(_Activity_)** ---> disable auto refreshing in webview on scrolling
  - **InkBookSDK.enableWebViewAutoRefresh(_Activity_)** ---> enable auto refreshing in webview on scrolling (default)
  - **InkBookSDK.refreshScreen(_Activity_)** ---> refresh device screen
  - **InkBookSDK.getPagesToRefresh(_Activity_)** ---> return pages count to refresh screen in reading mode


All this methods are used in test application in this repository.


