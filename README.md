A demonstration how to bridge VIGRA and ImgLib2
===============================================

The designs of the C++ library [VIGRA](http://hci.iwr.uni-heidelberg.de/vigra/)
and of the Java library [ImgLib2](http://imglib2.net/) resemble each other. This
is not a coincidence: VIGRA (among other C++ libraries) was the inspiration for
ImgLib2.

However, C++ and Java are different enough to make it relatively difficult to
reconcile both. This lil' project is an attempt to bridge these projects:
it demonstrates how to provide a VIGRA-backed ImgLib2 ImgFactory.
