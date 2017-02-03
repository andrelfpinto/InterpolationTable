package br.eng.andrelfpinto.interpolationtable

import spire.algebra.Field
import spire.algebra.Order
import spire.implicits._
import spire.math.ConvertableFrom
import spire.math.ConvertableTo

case class Indexable[T, U](get: (Seq[Int], T) => U)

object Indexable {
  implicit def recGetFn[T, U](
    implicit indexable: Indexable[T, U] = Indexable((Nil, value: T) => value)
  ) = Indexable((indices: Seq[Int], data: Seq[T]) => indexable.get(indices.tail, data(indices.head)))
}

case class InterpolationTable[I: ConvertableFrom: Field: Order, D](indices: Seq[Seq[I]], data: Seq[D]) {
  def getAtIndices[U](is: Seq[Int])(implicit indexable: Indexable[Seq[D], U]) = indexable.get(reindex(is), data)

  def apply[U: ConvertableFrom: ConvertableTo: Field](coordinates: Seq[I])(implicit indexable: Indexable[Seq[D], U]) = {
    val brps = coordinates.zip(indices).map { case (c, ix) => boundaryIndicesAndRelativePosition(c, ix) }

    val (boundaryIndices, relativePositions) = sequence(brps).get.unzip

    val valuesAtSurroudingGrid = combinations(boundaryIndices).map(getAtIndices[U])
    interpolateAll(valuesAtSurroudingGrid, relativePositions)
  }

  private def reindex[A](is: Seq[A]) = {
    val (i, t) = is.splitAt(2)
    t.reverse ++ i
  }

  private def boundaryIndicesAndRelativePosition(coordinate: I, index: Seq[I]) = {
    val allBoundaryIndices = (0 until index.size).sliding(2)
    val allBoundaries = index.sliding(2)

    allBoundaryIndices.zip(allBoundaries).collectFirst {
      case (boundaryIndices, Seq(lowerBoundary, higherBoundary))
        if lowerBoundary <= coordinate && coordinate <= higherBoundary => {
          val relativePosition = (coordinate - lowerBoundary) / (higherBoundary - lowerBoundary)
          (boundaryIndices, relativePosition)
        }
    }
  }

  private def sequence(s: Seq[Option[(Seq[Int], I)]]): Option[Seq[(Seq[Int], I)]] =
    if (s.contains (None)) None else Some(s.map(_.get))

  private def combinations[A](indices: Seq[Seq[A]]): Seq[Seq[A]] = {
    indices.reverse.foldLeft(Seq(Seq.empty[A])) {
      (x, y) => for (a <- x; b <- y) yield b +: a
    }
  }

  private def interpolateAll[A: Field: ConvertableFrom: ConvertableTo, B: Field: ConvertableFrom](values: Seq[A], relativePositions: Seq[B]): A = {
    val convert = implicitly[spire.math.ConvertableFrom[B]]
    val convertedRelativePositions: Seq[A] = relativePositions.map(convert.toType[A])

    convertedRelativePositions match {
      case Seq() => values.head
      case h :: t => {
        val newValues: Seq[A] = values.grouped(2).toList.map(interpolate(_, h))
        interpolateAll(newValues, t)
      }
    }
  }

  private def interpolate[A: Field](values: Seq[A], relativePosition: A) =
    values(0) * (1 - relativePosition) + values(1) * relativePosition
}
