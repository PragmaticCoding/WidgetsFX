package ca.pragmaticcoding.widgetsfx.layouts.images

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundSize

/**
 * Create an [ImageView] from an image file, scaled to fit a specified size.
 * @param url String path to the image location
 * @param fitWidth Double size for both height and width to scale the ImageView
 */
fun scaledImageViewFromUrl(url: String, fitWidth: Double) = ImageView(Image(url, true)).apply {
    this.fitWidth = fitWidth
    isPreserveRatio = true
}

/**
 * Create [Background] from an image file, stretched to fit the entire [Region]
 * @param url String path to the image location
 */
fun stretchedImageBackgroundFromUrl(url: String): Background {
    val backgroundSize = BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
    return (Background(BackgroundImage(Image(url, true), null, null, null, backgroundSize)))
}

