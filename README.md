A demonstration how to bridge VIGRA and ImgLib2
===============================================

The designs of the C++ library [VIGRA](http://hci.iwr.uni-heidelberg.de/vigra/)
and of the Java library [ImgLib2](http://imglib2.net/) resemble each other. This
is not a coincidence: VIGRA (among other C++ libraries) was the inspiration for
ImgLib2.

However, C++ and Java are different enough to make it relatively difficult to
reconcile both. This lil' project is an attempt to bridge these projects:
it demonstrates how to provide a VIGRA-backed ImgLib2 ImgFactory.

The challenges
==============

The major challenge is to access C++ data efficiently from Java. You see, Java
does not like the way C++ manages memory (it wants to be able to release it when
appropriate, or to shift things around, which C++ does not allow because it does
not keep track of memory references explicitly).

So the first stab at solving this problem is to access the C++-allocated data
structure only via explicit JNI functions. Should that turn out to be too slow,
we will have to look at using the JNI function
[```NewDirectByteBuffer```](http://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/functions.html#NewDirectByteBuffer)
in a new family of ```Img``` implementations that are still backed by
VIGRA-allocated data structures.

How to run this example
=======================

First make sure that you have built and installed
[VIGRA](http://hci.iwr.uni-heidelberg.de/vigra/).

Then clone the example: ```git clone https://github.com/dscho/vigra-imglib2```.
Then build it: ```mvn``` (if you do not have Maven yet, you might want to
[download and install it](http://maven.apache.org/).

After building it, you can run it with ```mvn exec:java```.

Running it inside Eclipse
-------------------------

Eclipse makes everything slightly more challenging, especially when you step
outside Eclipse's box even for an iota.

Therefore, you need to do everything as described
[above](#how-to-run-this-example) but instead of running it from the
command-line, you can now import the example as a Maven project via
'''File>Import>Import Existing Maven Project...'''.

Whenever you change the native code in ```src/main/cxx/```, you will have to
build from the command-line via ```mvn``` before you can try it in Eclipse.
