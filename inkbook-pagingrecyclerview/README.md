# inkBOOK PagingRecyclerView adaptation guide



#### Powered by  [![N|Solid](../logo_inkBOOK.jpg)](https://www.inkbook.eu/)

## Preface
Scrolling animations may not always look good on E-ink devices (depanding on scrollebale list items), due to low screen refresh rate. For this reason inkBOOK SDK provides modified version of RecyclerView, that could be used as alternative to scrolling and display long lists of items with the use of pagination instead of scrolling.



## How to add to a project

In order to use the library you just have to copy the .aar file from:

https://github.com/artatechsoft/InkbookSDK/tree/master/inkbook-pagingrecyclerview-library

To your application library folder. Then you can add the view to your layout like this:
```xml
   <pl.inkcompat.PagingRecyclerView
        android:id="@+id/list_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/emptyView"
        app:showCounterView="true" />
```
## Adapter

PagingRecyclerView.Adapter<RecyclerView.ViewHolder> works the same as RecyclerView.Adapter<RecyclerView.ViewHolder>. 
Example PagingRecyclerView.Adapter implementation can be found:

https://github.com/artatechsoft/InkbookSDK/blob/master/inkbook-pagingrecyclerview/app/src/main/java/pl/goltstein/staticscrollexample/RVLanguageAdapter.kt

## Layout Manager

PagingRecyclerView can be used with the same Layout Managers as standard Android RecyclerView.

## Changing Page

Paging recycler view has bottom navigation bar with buttons enabling user to change page. Pages can be also changed programmatically by calling adapter methods:

```java
adapter.onNextPage();
adapter.onPrevPage();
```
## Counter View

Counter view showing the items currently displayed on page and the number of items in the adapter, is by default displayed in the bottom navigation bar between page control buttons. It can be disabled by setting :

```xml
app:showCounterView="false"
```

