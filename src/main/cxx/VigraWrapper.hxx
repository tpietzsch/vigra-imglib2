#ifndef VIGRA_WRAPPER_
#define VIGRA_WRAPPER_

enum ImgLib2TypeId
{
	Byte = 0,
	UnsignedByte = 1,
	Int = 2,
	UnsignedInt = 3,
	Float = 4,
	Double = 5
};

#define IMGLIB_Int8   Byte
#define IMGLIB_UInt8  UnsignedByte
#define IMGLIB_Int32  Int
#define IMGLIB_UInt32 UnsignedInt
#define IMGLIB_float  Float
#define IMGLIB_double Double

#define IMGLIB_TYPE(T) IMGLIB_##T


/*
 * The following is all to create the macro CHOOSE, which is used as follows:
 * CHOOSE(F, arg1, ..., argn)
 * where you can have up to 9 args expands to
 * F(arg1) F(arg2) ... F(argn)
 */
#define CHOICE1(F, a)    F(a)
#define CHOICE2(F, a, b)    CHOICE1(F, a)       CHOICE1(F, b)
#define CHOICE3(F, a, b, c)    CHOICE2(F, a, b)    CHOICE1(F, c)
#define CHOICE4(F, a, b, c, d)    CHOICE3(F, a, b, c)    CHOICE1(F, d)
#define CHOICE5(F, a, b, c, d, e)    CHOICE4(F, a, b, c, d)    CHOICE1(F, e)
#define CHOICE6(F, a, b, c, d, e, f)    CHOICE5(F, a, b, c, d, e)    CHOICE1(F, f)
#define CHOICE7(F, a, b, c, d, e, f, g)    CHOICE6(F, a, b, c, d, e, f)    CHOICE1(F, g)
#define CHOICE8(F, a, b, c, d, e, f, g, h)    CHOICE7(F, a, b, c, d, e, f, g)    CHOICE1(F, h)
#define CHOICE9(F, a, b, c, d, e, f, g, h, i)    CHOICE8(F, a, b, c, d, e, f, g, h)    CHOICE1(F, i)
#define CHOOSER(   a, b, c, d, e, f, g, h, i, CHOICE, ...) CHOICE
#define CHOOSE(F,...) CHOOSER(__VA_ARGS__, CHOICE9, CHOICE8, CHOICE7, CHOICE6, CHOICE5, CHOICE4, CHOICE3, CHOICE2, CHOICE1)(F, __VA_ARGS__)

/*
 * Debuggin helper: STR(X) is the string to which macro X expands.
 */
#define STR_EXPAND(tok) #tok
#define STR(tok) STR_EXPAND(tok)

/*
 * generate a switch statement on a typeid and create cases for the given types.
 * Requires that a macro F is defined, which will be called as F(T) for each of the given types
 */
#define TYPE_CASE(T) \
	case IMGLIB_TYPE(T): {\
		std::cout << "case: " << STR(IMGLIB_TYPE(T)) << std::endl;\
		F(T);\
		break;\
	}
#define ALLOW_TYPES(TYPEID, ...) \
	switch (static_cast<ImgLib2TypeId>(TYPEID)) {\
		CHOOSE(TYPE_CASE, __VA_ARGS__)\
		default:\
			std::cerr << "not implemented for type id " << static_cast<ImgLib2TypeId>(TYPEID) << "." << std::endl;\
			break;\
	}

/*
 * generate a switch statement on an integer and create cases for the given values.
 * Requires that a macro F is defined, which will be called as F(N) for each of the given values.
 */
#define DIM_CASE(N) \
	case N: {\
		std::cout << "case: " << N << std::endl;\
		F(N);\
		break;\
	}
#define ALLOW_DIMENSIONS(N, ...) \
	switch (N) {\
		CHOOSE(DIM_CASE, __VA_ARGS__)\
		default:\
			std::cerr << "not implemented for " << N << " dimensions." << std::endl;\
			break;\
	}

#endif
