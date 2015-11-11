package org.scalacheck

import shapeless._
import _root_.derive.LowPriority

import derive._
import util._

trait SingletonInstances {
  implicit def arbitrarySingletonType[S]
   (implicit
     w: Witness.Aux[S]
   ): Arbitrary[S] =
    Arbitrary(Gen.const(w.value))
}

trait HListInstances {

  implicit def mkHListArbitrary[L <: HList]
   (implicit
     arb: MkHListArbitrary[L]
   ): Arbitrary[L] =
    arb.arbitrary

  implicit def mkHListShrink[L <: HList]
   (implicit
     arb: MkHListShrink[L]
   ): Shrink[L] =
    arb.shrink

}

trait CoproductInstances {

  implicit def mkCoproductArbitrary[C <: Coproduct]
   (implicit
     arb: MkCoproductArbitrary[C]
   ): Arbitrary[C] =
    arb.arbitrary

  implicit def mkCoproductShrink[C <: Coproduct]
   (implicit
     arb: MkCoproductShrink[C]
   ): Shrink[C] =
    arb.shrink

}

trait DerivedInstances {

  implicit def mkArbitrary[T]
   (implicit
     priority: Cached[Strict[LowPriority[
       Arbitrary[T],
       MkArbitrary[T]
     ]]]
   ): Arbitrary[T] =
    priority.value.value.value.arbitrary

  implicit def mkShrink[T]
   (implicit
     priority: Cached[Strict[LowPriority[
       Mask[Witness.`"Shrink.shrinkAny"`.T, Shrink[T]],
       MkShrink[T]
     ]]]
   ): Shrink[T] =
    priority.value.value.value.shrink

}

object Shapeless
  extends SingletonInstances
  with HListInstances
  with CoproductInstances
  with DerivedInstances
