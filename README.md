# NanoDDMS

This is a _nano_ version of Android Device Monitor (ADM, formerly known as DDMS). The only thing it can do is showing connected Android devices, Android processes with JDWP support, and JDWP debug ports. No heap info, threads, screenshots etc. 

## Motivation

From time to time I debug third-party Android APKs. I use Android Studio with [smalidea](https://github.com/JesusFreke/smali/wiki/smalidea) as [described in my blog](https://kov4l3nko.github.io/blog/2018-01-20-debugging-thirdparty-android-java-code/). To connect a debugger to a process, I need to know its JDWP port. 

I usually use Android Device Monitor (ADM, formerly known as DDMS) to get the JDWP port. Unfortunately, Google reduced ADM development/support as much as possible, so ADM is getting more and more obsolete and slowly dying. As result, ADM may not run properly with the latest JDK/MacOS. Sometimes downgrading JDK helps (see [Tip #1 in my blog post](https://kov4l3nko.github.io/blog/2018-01-20-debugging-thirdparty-android-java-code/)). However, it looks like one day ADM will stop working on modern MacOS. So I wrote the "nano" replacement.

## Thanks

NanoDDMS code is heavily inspired by [iSECPartners Android SSL Bypass](https://github.com/iSECPartners/android-ssl-bypass). Thank you, guys! :)
