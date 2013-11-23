#ifndef VIGRA_WRAPPER_HXX
#define VIGRA_WRAPPER_HXX

#include <vigra/sized_int.hxx>

/*
 * The following is all to create the macro CHOOSE, which is used as follows:
 *
 *    CHOOSE(F, arg1, ..., argn)
 *
 * where you can have up to 10 args expands to
 *
 *    F(arg1) F(arg2) ... F(argn)
 *
 * The design is somewhat complex to make it run on Visual Studio as well
 */

#define CHOICE_COUNT_ARGS_IMPL2(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, count, ...)  count
#define CHOICE_COUNT_ARGS_IMPL1(args)  CHOICE_COUNT_ARGS_IMPL2 args
#define CHOICE_COUNT_ARGS(...)  CHOICE_COUNT_ARGS_IMPL1((__VA_ARGS__, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0))
   
#define CHOICE_HELPER_IMPL(...) CHOICE##__VA_ARGS__
#define CHOICE_HELPER(count) CHOICE_HELPER_IMPL(count)

#define CHOICE_GLUE(a, b) a b

#define CHOICE1(F, a)    F(a)
#define CHOICE2(F, a, b)    CHOICE1(F, a)  CHOICE1(F, b)
#define CHOICE3(F, a, b, c)    CHOICE1(F, a) CHOICE2(F, b, c)
#define CHOICE4(F, a, b, c, d)    CHOICE1(F, a) CHOICE3(F, b, c, d)
#define CHOICE5(F, a, b, c, d, e)    CHOICE1(F, a) CHOICE4(F, b, c, d, e)
#define CHOICE6(F, a, b, c, d, e, f)    CHOICE1(F, a) CHOICE5(F, b, c, d, e, f)
#define CHOICE7(F, a, b, c, d, e, f, g)    CHOICE1(F, a) CHOICE6(F, b, c, d, e, f, g)
#define CHOICE8(F, a, b, c, d, e, f, g, h)    CHOICE1(F, a) CHOICE7(F, b, c, d, e, f, g, h)
#define CHOICE9(F, a, b, c, d, e, f, g, h, i)    CHOICE1(F, a) CHOICE8(F, b, c, d, e, f, g, h, i)
#define CHOICE10(F, a, b, c, d, e, f, g, h, i, j)    CHOICE1(F, a) CHOICE9(F, b, c, d, e, f, g, h, i, j)
#define CHOOSER(...) __VA_ARGS__
#define CHOOSE(F, ...)  CHOOSER(CHOICE_GLUE(CHOICE_HELPER(CHOICE_COUNT_ARGS(__VA_ARGS__)), (F, __VA_ARGS__)))

/*
 * Debugging helper: STR(X) is the string to which macro X expands.
 */
#define STR_EXPAND(tok) #tok
#define STR(tok) STR_EXPAND(tok)

/*
 * generate a switch statement on a typeid and create cases for the given types.
 * Requires that a macro F is defined, which will be called as F(T) for each of the given types
 */
#define TYPE_CASE(T) \
	case imglib2::TypeTraits<T>::id: {\
		std::cout << "case: " << imglib2::TypeTraits<T>::name() << std::endl;\
		F(T);\
		break;\
	}
#define ALLOW_TYPES(TYPEID, ...) \
	switch (static_cast<imglib2::TypeId>(TYPEID)) {\
		CHOOSE(TYPE_CASE, __VA_ARGS__)\
		default:\
			std::cerr << "not implemented for type '" << imglib2::id2name(static_cast<imglib2::TypeId>(TYPEID)) << "'.\n"; \
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
    
namespace imglib2 {

enum TypeId
{
	Int8 = 0,
	UInt8 = 1,
	Int32 = 2,
	UInt32 = 3,
	Float = 4,
	Double = 5,
    Unknown
};

template <class T>
struct TypeTraits;

#define IMGLIB2_TYPETRAITS(type, type_id) \
template <> \
struct TypeTraits<type> \
{ \
    static const TypeId id = type_id; \
    static std::string name() { return #type_id ; } \
};

IMGLIB2_TYPETRAITS(vigra::Int8,   Int8)
IMGLIB2_TYPETRAITS(vigra::UInt8,  UInt8)
IMGLIB2_TYPETRAITS(vigra::Int32,  Int32)
IMGLIB2_TYPETRAITS(vigra::UInt32, UInt32)
IMGLIB2_TYPETRAITS(float, Float)
IMGLIB2_TYPETRAITS(double, Double)

#undef IMGLIB2_TYPETRAITS

inline std::string id2name(TypeId id)
{
    switch(id)
    {
        #define F(T) return TypeTraits<T>::name()
        CHOOSE(TYPE_CASE, vigra::Int8, vigra::UInt8, vigra::Int32, vigra::UInt32, float, double)
        #undef F
        default:
            return "unknown type";
    }
}

} // namespace imglib2

#endif // VIGRA_WRAPPER_HXX
