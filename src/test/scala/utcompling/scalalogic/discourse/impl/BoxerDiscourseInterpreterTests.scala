package utcompling.scalalogic.discourse.impl

import utcompling.scalalogic.discourse.candc.boxer.expression.interpreter.impl.PassthroughBoxerExpressionInterpreter
import utcompling.scalalogic.discourse.candc.call._
import utcompling.scalalogic.discourse.candc.call.impl._
import utcompling.scalalogic.drt.expression.DrtExpression
import utcompling.scalalogic.util.FileUtils
import org.junit.Test
import org.junit.Test

class BoxerDiscourseInterpreterTests {

  @Test
  def test() {

    val binDir = Some(FileUtils.pathjoin(System.getenv("HOME"), "bin/candc/bin"))
    val candc = CandcImpl.findBinary(binDir)
    val boxer = BoxerImpl.findBinary(binDir)
    val bi = new BoxerDiscourseInterpreter(candc = candc, boxer = boxer)

    val fakeCandc = new FakeCandc(
      """% this file was generated by the following command(s):
%   /Users/dhg/workspace/candc/bin/candc --models /Users/dhg/workspace/candc/bin/../models/boxer --candc-printer boxer

:- op(601, xfx, (/)).
:- op(601, xfx, (\)).
:- multifile ccg/2, id/2.
:- discontiguous ccg/2, id/2.

ccg(1,
 rp(s:dcl,
  ba(s:dcl,
   fa(np:nb,
    t(np:nb/n, 'Every', 'every', 'DT', 'I-NP', 'O'),
    t(n, 'man', 'man', 'NN', 'I-NP', 'O')),
   fa(s:dcl\np,
    t((s:dcl\np)/np, 'loves', 'love', 'VBZ', 'I-VP', 'O'),
    fa(np:nb,
     t(np:nb/n, 'a', 'a', 'DT', 'I-NP', 'O'),
     t(n, 'woman', 'woman', 'NN', 'I-NP', 'O')))),
  t(period, '.', '.', '.', 'O', 'O'))).

id('0', [1]).

""")
    val drs1 = new BoxerDiscourseInterpreter(
      candc = fakeCandc,
      boxer = boxer).interpret("Every man loves a woman .")
    println(drs1.pretty)

    //        val drs2 = bi.interpret("Every man loves a woman .")
    //        drs2.pprint()
    //
    //        val drs3 = bi.batchInterpret(List("Every man loves a woman .", "A man walks .", "A woman walks ."))
    //        drs3.foreach(_.foreach(_.pprint()))
    //
    //        val drs4 = bi.interpret("John forgot to call Bill .")
    //        println(drs4)
    //        println(drs4.pretty)
    //        println(drs4.folModal)
    //        println(drs4.folModal.pretty)
    //
    //        println(bi.interpret("Fido is a dog .").pretty)
    //        println(bi.interpret("Every dog walks .").pretty)
    //        println(bi.interpretMultisentence(List("Fido is a dog .", "Every dog walks .")).pretty)
    bi.batchInterpret(List("Fido is a dog .", "Every dog walks .")).map(_.map(d => println(d.pretty)))

  }

  //    class CandcOutputCatcher extends CandcImpl {
  //        private var output: Option[String] = None
  //        override protected def call(inputStr: Option[String], args: List[String] = List(), verbose: Boolean = false): String = {
  //            this.output = Some(super.call(inputStr, args, verbose))
  //            return this.output.get
  //        }
  //        def getOutput() =
  //            this.output
  //    }

  class FakeCandc(output: String) extends Candc {
    override def batchParseMultisentence(inputs: List[List[String]], args: Map[String, String] = Map(), discourseIds: Option[Seq[String]] = None, model: Option[String] = None, verbose: Boolean = false): String =
      this.output
  }

}
