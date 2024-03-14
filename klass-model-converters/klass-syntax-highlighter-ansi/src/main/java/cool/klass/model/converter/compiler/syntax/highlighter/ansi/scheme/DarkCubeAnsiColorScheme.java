/*
 * Copyright 2024 Craig Motlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public enum DarkCubeAnsiColorScheme
        implements AnsiColorScheme
{
    INSTANCE;

    @Override
    public void background(Ansi ansi)
    {
        ansi.bg(Color.BLACK);
    }

    @Override
    public void blockComment(Ansi ansi)
    {
        ansi.fg(246);
    }

    @Override
    public void keyword(Ansi ansi)
    {
        ansi.fg(166);
    }

    @Override
    public void verb(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void modifier(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void identifier(Ansi ansi)
    {
        ansi.fg(162);
    }

    @Override
    public void propertyName(Ansi ansi)
    {
        ansi.fg(134);
    }

    @Override
    public void literal(Ansi ansi)
    {
        ansi.fg(74);
    }

    @Override
    public void punctuation(Ansi ansi)
    {
        ansi.fg(251);
    }

    @Override
    public void operator(Ansi ansi)
    {
        ansi.fg(Color.MAGENTA);
    }

    @Override
    public void urlConstant(Ansi ansi)
    {
        this.literal(ansi);
    }
}
