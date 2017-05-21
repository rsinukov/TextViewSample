# Square TextView with animatable background

## About
The project consists of 2 modules: 
1. `lib` where `AnimatedSquareTextView` is located
2. `app` with sample usage


Default animation is gradient from *YELLOW* to *RED* through *BLUE*  
Every 2 seconds the order of the colours is reversed and the reversal
is animated across 500ms.

## Configuation
You can configure bg animation via custom attrs:

1. Create array of drawables, ex:
```xml
    <array name="gradient">
        <item>@drawable/gradient_1</item>
        <item>@drawable/gradient_2</item>
    </array>
```
2. Change animation duration and animated drawables in your layout xml:
```xml
 <com.example.lib.AnimatedSquareTextView
        android:id="@+id/activity_main_square_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="8dp"
        android:gravity="center"
        app:items="@array/gradient"
        app:staticDuration="2000"
        app:transitionDuration="500" />
```