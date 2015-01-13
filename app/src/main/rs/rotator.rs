/*
* Copyright (C) 2011 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

#pragma version(1)
#pragma rs java_package_name(ua.kulku.rs)

rs_allocation inImage;
int inWidth;
int inHeight;
int direction = 0;
const rotate_90_clockwise = 90;
const rotate_180_clockwise = 180;
const rotate_270_clockwise = 270;
const flip_horizontally = 3;
const flip_vertically = 4;

uchar4 __attribute__ ((kernel)) transform (uchar4 in, uint32_t x, uint32_t y) {
    uint32_t inX;
    uint32_t inY;
    
    switch(direction)
    {
        case rotate_90_clockwise:
        inX  = inWidth - 1 - y;
        inY = x;
        break;

        case rotate_180_clockwise:
        inX = inWidth - 1 - x;
        inY = inHeight - 1 - y;
        break;

        case rotate_270_clockwise:
        inX  = y;
        inY = inHeight - 1 - x;
        break;

        case flip_horizontally:
        inX  =  inWidth - 1 - x;
        inY = y;
        break;

        case flip_vertically:
        inX  = x;
        inY =  inHeight - 1 - y;
        break;
    }
    
    const uchar4 *out = rsGetElementAt(inImage, inX, inY);
    return *out;
}