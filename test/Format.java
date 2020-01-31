/*
 * Copyright 2018 Thinh Pham.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 * @author Thinh Pham
 */
public class Format {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(formatFloat(1.99999998f, 4));
    }
    
    private static String formatFloat(Float value, int limit) {
        String str = String.format("%1$."+limit+"f", value);
        Float flt = Float.parseFloat(str);
        Integer rnd = Math.round(flt);
        if (Math.abs(flt - rnd) > 0.00001f)
            return flt.toString();
        else
            return rnd.toString();
    }
    
}
