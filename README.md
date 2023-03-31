# inkBOOK software adaptation guide



#### Powered by  [![N|Solid](logo_inkBOOK.jpg)](https://www.inkbook.eu/)

## Preface
This document gathers all information useful for adopting Android applications to be used on inkBOOK  E Ink devices.


## User Interface simplifications
### Widget background 

Dark backgrounds and colorful gradients should be avoided.  When graphic is scaled to 16-level grayscale this may lead to unreadable text sections or even in most drastic scenarios to displaying black text on black background. It is highly recommended to follow guidelines form Recommended colour range section.
### Animation effects
All pageturn and other animation effects should be removed. Rich animated interface will not be displayed properly in E Ink environment. This may lead to unpleasant screen flickering, random erratic screen refreshments or at least odd behaviour of interface components.
### Item lists - scrolling vs pagination
inkBOOK Android system is customised for E Ink and can handle scroll events animation effects. However using scrolled lists in interface and especially scrolled content in activity windows is not recommended. In order to display scroll animation properly screen needs to change display mode form 16-level grayscale to black and white only or 4-level grayscale and then go back to 16-level grayscale again when animation ends. Interfaces and content with reduced grayscale does not look well, looses all of details and changing screen mode requires full screen refresh each time, which results in unpleasant screen flickering. 
It is highly recommended to use list pagination instead of scrolling as often as possible.

We have made available custom paging recycler view, that can be used to replace Android RecyclerView.
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
Because of nature of E Ink screen sometimes scaled images might appear more blurry than the same images used as tablet/smartphone UI elements. It is highly recommended to perform visual verification of all non-SVG  UI elements and icons prior to application release. If some of graphic are blurry, we recommend to prepare them in exact required resolution in order to avoid software scaling down or scaling up. Current screen parameters are:

| Model             | Resolution  | Pixel Density |
|-------------------|-------------|---------------|
| inkBOOK Calypso plus | 1024x758 px  | 212 dpi       |
| inkBOOK Focus     | 1872x1404 px | 300 dpi       |
 
## Power management features
### Background synchronization
In order to keep power usage optimal all background data processing should be limited to absolute minimum to avoid high CPU loads and allow system to enter Idle state as often as possible.
Additionally WiFi connectivity should never be used for extended period of time and especially it’s not recommended to keep WiFi connection up artificially (WiFi automatically powers down after few minutes of inactivity). During WiFi data transfer inkBOOK power usage might increase several times above normal battery consumption. As result extensive WiFi usage may reduce weeks of battery lifetime to merely days. 
### Idle mode
The significant difference between reading apps designed to be used on smartphone/tablet and e-reader is how the Idle mode is handled. Devices using standard LCD screens tend to dim or power down screen when entering Idle mode. This is undesired behaviour, so Idle mode is often being disabled in reader app. E-reader screen does not require any power to keep it’s current state, so this problem does not exist at all.
In order to keep power usage optimal it is highly recommended to remove or disable all code preventing Android from entering Idle mode. Example:
```java
myWakeLock.acquire();  // prevent Idle
...
myWakeLock.release();  // allow Idle
```
Neglecting proper Idle mode management may lead to increase of battery usage up to 3 times more than normal.
### Device identification
It is understandable that developing separate Android application just for e-reader usage might not be the best business approach. In order to make developing single app for inkBOOK, smartphones, tablets possible, SDK has been equiped with the method that alows very easily to identify inkBOOK e-reader and relay on this information to diversify crucial parts of code and user interfaces.

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
Major diffrence between e-paper and other types of displays is managing refresh. E-Ink screen does not automaticaly refresh. It is refreshed by system only when it's context changes. 
Additionaly, screen may refresh itself using different modes/strategies that prioritise different parameters like speed, image quality, ghosting effects (discussed later on).

#### Reading mode

Reading mode or Full refresh mode, use full capabilities of 16-level grayscale to refresh the screen. This mode has the slowest refresh speed (450-520ms), with less residue but it may cause flickering effect when displaying more dynamic content. Can be also used to periodically clear the residue/ghosting caused by the refresh of other modes. This mode should be used to display text and high quality grayscale images.

Example code to invoke this mode:

```kotlin
fun changeMode(view : View, mode : Int){ 
    InkBookSDK.refreshView(view, EInkRefreshUtil.EPD_FULL)
}

fun changeMode(activity : Activity, mode : Int){
    InkBookSDK.refresh(activity, EInkRefreshUtil.EPD_FULL)
}

```
Remark:
Please note that changeMode() may consume either View or Activity object, but refresh mode change will affect whole screen.

#### Scrolling mode

Scrolling mode also known as A2 mode, is generally used to display dynamic content like video, motion picture, dynamic UI components and other scenarios. In current implemantation it reduces grayscale from 16 to 4-levels. This allows to perform much faster screen updates (around 120-200 ms) by sacrificing detail of displayed image. 

Example code to invoke this mode

```kotlin
fun changeMode(view : View, mode : Int){ 
    InkBookSDK.refreshView(view, EInkRefreshUtil.EPD_A2)
}

fun changeMode(activity : Activity, mode : Int){
    InkBookSDK.refresh(activity, EInkRefreshUtil.EPD_A2)
}

```
Please note, that invoking this mode could be useful to display scrollable menus.

Remark:
Please note that changeMode() may consume either View or Activity object, but refresh mode change will affect whole screen.

#### Forcing full screen refresh

In order to avoid unwanted so called ghosting effect (when part of previous screen context remains as a shadow on the screen), it is necessary to perform full screen refresh from time to time. Full screen refresh causes black and white blink on screen and uses more power when normal partial refresh, so it should be used  with reasonable aproach. User defines prefered fullscreen refresh interval in device Settings quantified as number of page turns until full screen refresh. This action is managed automaticaly by the system. However, in some rare scenarious it may be nessesary to invoke full screen refresh manually to improve user expariance and clear ghosting effects form the screen.

To invoke full screen refresh simply force realoding currently used screen refresh mode. Example:

```kotlin
    fun changeMode(activity : Activity){
        InkBookSDK.refresh(activity, EInkRefreshUtil.EPD_FULL)
    }
```

#### using dark mode (night mode)

Extensive use of dark backgrounds may increase ghosting efects on the screen. In order to mitigate negative effects and improve user experiance inkBOOK is using separate screen setting prest, that is temporary disabling full refresh setting defined by user (in device Settings).
Each time high contrast night mode (white text on black background) is used in reading app, night mode should also be activated and disabled acordingly in inkBOOK SDK. It is also highly recomended to force full screen refresh after each page turn in dark mode.

This could be done by using methods:

```kotlin
   
   InkBookSDK.enterNightMode(__Activity__);
   InkBookSDK.exitNightMode(__Activity__);
    
```


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

# inkBookSDK methods referance

  - **InkBookSDK.isInkBook()** ---> true if device is inkBOOK
  - **InkBookSDK.refreshView(__View__, __Int__)** ---> refresh view by introduced mode
  - **InkBookSDK.refresh(__Activity__, __Int__)** ---> refresh device screen (Activity) by introduced mode
  - **InkBookSDK.getBrightness(__Activity__)** --> return current value of built-in light brightness
  - **InkBookSDK.getTemperature(__Activity__)** --> return current value of built-in light temperature (ratio between cold and warm light)
  - **InkBookSDK.setBrightness(__Activity__, __Int__)** --> change built-in light brightness to introduced value (max 255)
  - **InkBookSDK.setTemperature(__Activity__, __Int__)** --> change built-in light temperature to introduced value (max 255)
  - **InkBookSDK.enterNightMode(__Activity__)** --> activate night mode screen refresh strategy
  - **InkBookSDK.exitNightMode(__Activity__)** --> deactivate night mode screen refresh strategy


## inkBookSDK refresh modes referance (used by InkBookSDK.refresh() and InkBookSDK.refreshView())

```java
class EInkRefreshUtil {
public static int EPD_FULL=1;
public static int EPD_A2=2;
}
```

# LIST OF COMPATIBLE DEVICES:
1. inkBOOK Calypso plus 6"
2. inkBOOK Focus 7,8"



# inkBOOK PagingRecyclerView adaptation guide



#### Powered by  [![N|Solid](../logo_inkBOOK.jpg)](https://www.inkbook.eu/)

## Preface
Scrolling animations may not always look good on E-ink devices (depanding on scrollebale list items), due to low screen refresh rate. For this reason inkBOOK SDK provides modified version of RecyclerView, that could be used as alternative to scrolling and display long lists of items with the use of pagination instead of scrolling.


In order to use the library you just have to copy the .aar file from:

https://github.com/artatechsoft/InkbookSDK/tree/master/inkbooksdk

To your application library folder. Then you can add the view to your layout like this:
```xml
   <com.artatech.android.shared.customview.inkbookpaging.InkBookPagingRecyclerView
        android:id="@+id/list_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Example implementation
'''
binding.recyclerView.initLayout<String>(
    object : InkRecyclerView.InkRecyclerViewInterface<String> {
    override fun createListViewHolder(
        viewGroup: ViewGroup?,
        viewType: Int
    ): InkAbstractViewHolder<String> {
        TitleListItemViewHolder(
            ItemListBinding.inflate(
            LayoutInflater.from(context), viewGroup, false
            )
    )
}

override fun createGridViewHolder(
    viewGroup: ViewGroup?,
    viewType: Int
): InkAbstractViewHolder<String> {
        TitleGridItemViewHolder(
            ItemGridBinding.inflate(
                LayoutInflater.from(context), viewGroup, false
            )
        )
    }
}, ::filterList, InkRecyclerView.Mode.LIST
)


fun updateData(list: List<String>) {
    binding.recyclerView.setData(list)
}

private fun filterList(query: String, item: String): Boolean {
    return item.contains(query)
}

class TitleListItemViewHolder(binding: ItemListBinding) :
InkAbstractViewHolder<String>(binding.root) {
    override fun bind(item: String?) {
        binding.title.text = item
    }
}

class TitleGridItemViewHolder(binding: ItemGridBinding) :
InkAbstractViewHolder<String>(binding.root) {
    override fun bind(item: String?) {
        binding.title.text = item
    }
}
'''

