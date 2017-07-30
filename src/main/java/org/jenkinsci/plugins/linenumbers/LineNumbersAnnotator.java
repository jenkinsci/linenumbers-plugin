/*
 * The MIT License
 * 
 * Copyright (c) 20import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import hudson.model.Run;

import java.text.MessageFormat;
d associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.linenumbers;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import hudson.model.Run;

import java.text.MessageFormat;

public class LineNumbersAnnotator extends ConsoleAnnotator<Object> {

  private int                  calls            = 0;

  private static final long    serialVersionUID = 1L;

  private long                 offset;

  public LineNumbersAnnotator(long offset) {
    this.offset = offset;
  }

  @Override
  public ConsoleAnnotator annotate(Object context, MarkupText text) {
    if (!(context instanceof Run)) {
      return this;
    }
    Run r = (Run)context;
    long start;
    if (offset < 0) {
      start = r.getLogFile().length() + offset;
    } else {
      start = offset;
    }
    if (start <= 0) { // only annotate if we are handling the full log
      calls++;
      int end = text.length() - 1;

      if (text.getText().matches("^\\s$")) {
        // Trick to make sure we wrap everything including the Timestamp from Timestamper plugin
        text.addMarkup(0, 0, "",
            MessageFormat.format("<p class=\"empty\"><a class=\"linenumber\" id=\"L{0}\" href=\"#L{0}\"></a><span>", calls));
        text.addMarkup(end, end, "", "</span></p>");
      } else {
        text.addMarkup(0, 0, "",
            MessageFormat.format("<p class=\"line\"><a class=\"linenumber\" id=\"L{0}\" href=\"#L{0}\"></a><span>", calls));
        text.addMarkup(end, end, "", "</span></p>");
      }
    }
    return this;
  }

}
