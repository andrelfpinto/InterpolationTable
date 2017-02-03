package br.eng.andrelfpinto.interpolationtable

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._
import spire.implicits.DoubleAlgebra

class InterpolationTableSpec extends FlatSpec with Matchers {
  implicit val doubleTolerance = org.scalactic.TolerantNumerics.tolerantDoubleEquality(1e-3)

  val indices = Seq(
    Seq( 0.0,  1.0,  2.0),
    Seq(10.0, 11.0, 12.0),
    Seq(20.0, 21.0, 22.0)
  )

  val data = Seq(
    Seq(Seq( 1.0,  2.0,  3.0),
        Seq( 4.0,  5.0,  6.0),
        Seq( 7.0,  8.0,  9.0)),
    Seq(Seq(11.0, 12.0, 13.0),
        Seq(14.0, 15.0, 16.0),
        Seq(17.0, 18.0, 19.0)),
    Seq(Seq(21.0, 22.0, 23.0),
        Seq(24.0, 25.0, 26.0),
        Seq(27.0, 28.0, 29.0))
  )

  val table1D = InterpolationTable(indices.take(1), data(0)(0))
  val table2D = InterpolationTable(indices.take(2), data(0))
  val table3D = InterpolationTable(indices, data)

  "An InterpolationTable" should "get values from the grid at indices" in {
    val indices = Table(
      ("indices",    "result"),
      (Seq(0, 0, 0),      1.0),
      (Seq(0, 1, 0),      2.0),
      (Seq(0, 2, 0),      3.0),
      (Seq(1, 0, 0),      4.0),
      (Seq(1, 1, 0),      5.0),
      (Seq(1, 2, 0),      6.0),
      (Seq(2, 0, 0),      7.0),
      (Seq(2, 1, 0),      8.0),
      (Seq(2, 2, 0),      9.0),
      (Seq(0, 0, 1),     11.0),
      (Seq(0, 1, 1),     12.0),
      (Seq(0, 2, 1),     13.0),
      (Seq(1, 0, 1),     14.0),
      (Seq(1, 1, 1),     15.0),
      (Seq(1, 2, 1),     16.0),
      (Seq(2, 0, 1),     17.0),
      (Seq(2, 1, 1),     18.0),
      (Seq(2, 2, 1),     19.0),
      (Seq(0, 0, 2),     21.0),
      (Seq(0, 1, 2),     22.0),
      (Seq(0, 2, 2),     23.0),
      (Seq(1, 0, 2),     24.0),
      (Seq(1, 1, 2),     25.0),
      (Seq(1, 2, 2),     26.0),
      (Seq(2, 0, 2),     27.0),
      (Seq(2, 1, 2),     28.0),
      (Seq(2, 2, 2),     29.0)
    )

    forAll(indices) { (indices: Seq[Int], result: Double) =>
      table3D.getAtIndices(indices) should equal (result)
    }
  }

  it should "interpolate the values from the grid at known points (1D)" in {
    val positions = Table(
      ("position", "result"),
      (Seq(0.0),        1.0),
      (Seq(1.0),        2.0),
      (Seq(2.0),        3.0)
    )

    forAll(positions) { (position: Seq[Double], result: Double) =>
      table1D.apply[Double](position) should equal (result)
    }
  }

  it should "interpolate the values from the grid at known points (2D)" in {
    val positions = Table(
      ("position",     "result"),
      (Seq(0.0, 10.0),      1.0),
      (Seq(0.0, 11.0),      2.0),
      (Seq(0.0, 12.0),      3.0),
      (Seq(1.0, 10.0),      4.0),
      (Seq(1.0, 11.0),      5.0),
      (Seq(1.0, 12.0),      6.0),
      (Seq(2.0, 10.0),      7.0),
      (Seq(2.0, 11.0),      8.0),
      (Seq(2.0, 12.0),      9.0)
    )

    forAll(positions) { (position: Seq[Double], result: Double) =>
      table2D.apply[Double](position) should equal (result)
    }
  }

  it should "interpolate the values from the grid at known points (3D)" in {
    val positions = Table(
      ("position",           "result"),
      (Seq(0.0, 10.0, 20.0),      1.0),
      (Seq(0.0, 11.0, 20.0),      2.0),
      (Seq(0.0, 12.0, 20.0),      3.0),
      (Seq(1.0, 10.0, 20.0),      4.0),
      (Seq(1.0, 11.0, 20.0),      5.0),
      (Seq(1.0, 12.0, 20.0),      6.0),
      (Seq(2.0, 10.0, 20.0),      7.0),
      (Seq(2.0, 11.0, 20.0),      8.0),
      (Seq(2.0, 12.0, 20.0),      9.0),
      (Seq(0.0, 10.0, 21.0),     11.0),
      (Seq(0.0, 11.0, 21.0),     12.0),
      (Seq(0.0, 12.0, 21.0),     13.0),
      (Seq(1.0, 10.0, 21.0),     14.0),
      (Seq(1.0, 11.0, 21.0),     15.0),
      (Seq(1.0, 12.0, 21.0),     16.0),
      (Seq(2.0, 10.0, 21.0),     17.0),
      (Seq(2.0, 11.0, 21.0),     18.0),
      (Seq(2.0, 12.0, 21.0),     19.0),
      (Seq(0.0, 10.0, 22.0),     21.0),
      (Seq(0.0, 11.0, 22.0),     22.0),
      (Seq(0.0, 12.0, 22.0),     23.0),
      (Seq(1.0, 10.0, 22.0),     24.0),
      (Seq(1.0, 11.0, 22.0),     25.0),
      (Seq(1.0, 12.0, 22.0),     26.0),
      (Seq(2.0, 10.0, 22.0),     27.0),
      (Seq(2.0, 11.0, 22.0),     28.0),
      (Seq(2.0, 12.0, 22.0),     29.0)
    )

    forAll(positions) { (position: Seq[Double], result: Double) =>
      table3D.apply[Double](position) should equal (result)
    }
  }
}
