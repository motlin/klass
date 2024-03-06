/*
 * Copyright 2020 Craig Motlin
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

package cool.klass.model.meta.domain.json.dto;

public abstract class BinaryCriteriaDTO
        implements CriteriaDTO
{
    private final CriteriaDTO left;
    private final CriteriaDTO right;

    protected BinaryCriteriaDTO(CriteriaDTO left, CriteriaDTO right)
    {
        this.left  = left;
        this.right = right;
    }

    public CriteriaDTO getLeft()
    {
        return this.left;
    }

    public CriteriaDTO getRight()
    {
        return this.right;
    }
}
