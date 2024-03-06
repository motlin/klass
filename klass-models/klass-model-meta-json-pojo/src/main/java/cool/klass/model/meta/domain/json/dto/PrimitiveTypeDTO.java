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

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PrimitiveTypeDTO
{
    @JsonProperty("Integer")
    INTEGER,
    @JsonProperty("Long")
    LONG,
    @JsonProperty("Double")
    DOUBLE,
    @JsonProperty("Float")
    FLOAT,
    @JsonProperty("Boolean")
    BOOLEAN,
    @JsonProperty("String")
    STRING,
    @JsonProperty("Instant")
    INSTANT,
    @JsonProperty("LocalDate")
    LOCAL_DATE,
    @JsonProperty("TemporalInstant")
    TEMPORAL_INSTANT,
    @JsonProperty("TemporalRange")
    TEMPORAL_RANGE,
}
