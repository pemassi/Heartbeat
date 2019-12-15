package io.pemassi.heartbeat.configurations

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * DataSource Configuration Class
 *
 * This class is for excluding data source auto configuration.
 * Since we are not going to use for now.
 *
 * @author Kyungyoon Kim (ruddbs5302@gmail.com)
 */
@Configuration
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class DataSourceConfiguration