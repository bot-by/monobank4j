/*
 * Copyright 2021-2023 Witalij Berdinskich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.bot_by.monobank4j.api_jackson_jr;

import com.fasterxml.jackson.jr.ob.JacksonJrExtension;
import com.fasterxml.jackson.jr.ob.api.ExtensionContext;
import feign.Feign;
import feign.RequestLine;
import feign.jackson.jr.JacksonJrDecoder;
import java.util.List;

/**
 * Get currency rates. It is part of Monobank Public API.
 *
 * <h3>How to get currency rates</h3>
 * <p>
 * First create an instance of Monobank Currency API.
 * <p>
 * You can use static method {@linkplain #getInstance} or build it with
 * <a href="https://github.com/OpenFeign/feign">Feign</a> manually,
 * e.g. if you want to use custom client:
 * <pre><code class="language-java">
 * List&lt;JacksonJrExtension&gt; extensions = singletonList(new JacksonJrExtension() {
 *     {@literal @}Override
 *     protected void register(ExtensionContext context) {
 * 	       context.insertProvider(new UnixTimeProvider());
 *     }
 * });
 *
 * currency = Feign.builder()
 *                 .client(new Http2Client())
 *                 .decoder(new JacksonJrDecoder(extensions))
 *                 .target(Currency.class, "https://api.monobank.ua/");
 * </code></pre>
 * <p>
 * Then just get currency rates
 * <pre><code class="language-java">
 * List&lt;CurrencyInfo&gt; currencyExchangeRates = currency.getRates();
 * </code></pre>
 *
 * @see <a href="https://api.monobank.ua/docs/#operation--bank-currency-get">Monobank API: отримання
 * курсів валют</a>
 */
public interface Currency {

  /**
   * Get an instance of currency Monobank Currency API.
   *
   * @return a currency API instance
   */
  static Currency getInstance() {
    return Feign.builder()
        .decoder(new JacksonJrDecoder(List.of(new JacksonJrExtension() {
          @Override
          protected void register(ExtensionContext context) {
            context.insertProvider(new UnixTimeProvider());
          }
        })))
        .target(Currency.class, "https://api.monobank.ua/");
  }

  /**
   * Get a list of Monobank's exchange rates.
   * <p>
   * <strong>Important:</strong><br>
   * the data are cached and updated not more than once every 5 minutes.
   *
   * @return list of currency rates
   * @see CurrencyInfo
   */
  @RequestLine("GET /bank/currency")
  List<CurrencyInfo> getRates();

}
