# ImageRotation
Comparison of the different ways of the rotated image creation on Android (90, 180, 270 degrees). Targeting performance and heap memory usage minimization.

* Bitmap.createBitmap()
* RenderScript
* NDK library from AndroidDeveloperLB
* Splitting into rotated pieces, saving into the files, combining these files in the new order.
* [TODO] OpenGL
