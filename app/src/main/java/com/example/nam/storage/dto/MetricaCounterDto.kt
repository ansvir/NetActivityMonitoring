package com.example.nam.storage.dto

import com.google.gson.annotations.SerializedName

data class MetricaCounterDto(val counter: MetricaCounterRequestDto)

data class MetricaCounterInfoDto(val rows: Int, val counters: List<MetricaCounterResponseDto>?)

data class MetricaCounterRequestDto(
    val name: String?,
    val site2: Site?,
    val gdpr_agreement_accepted: Boolean = true,
    val mirrors2: List<Site> = listOf(),
    val goals: List<String> = listOf(),
    val filters: List<Filter> = listOf(),
    val operations: List<Operation> = listOf(),
    val grants: List<Grant> = listOf(),
    val labels: List<Label> = listOf(),
    val webvisor: Webvisor? = null,
    val code_options: CodeOptions? = null,
    val create_time: String? = null,
    val time_zone_name: String = "(GMT+03:00) Минск",
    val time_zone_offset: Int? = null,
    val source: CounterSource? = null,
    val monitoring: Monitoring? = null,
    val filter_robots: Int? = null,
    val visit_threshold: Int? = null,
    val offline_options: OfflineOptions? = null,
    val publisher_options: PublisherOptions? = null,
    val autogoals_enabled: Boolean = true,
    val counter_flags: CounterFlags? = null
)

data class MetricaCounterResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("activity_status")
    val activity_status: String,
    @SerializedName("site2")
    val site2: Site?
)

data class MetricaPageViewsResponseDto(
    @SerializedName("total_rows")
    val totals: Long?
)

data class Site(
    val site: String?
)

data class Filter(
    val attr: FilterAttribute,
    val type: FilterType,
    val value: String,
    val action: FilterAction,
    val status: FilterStatus,
    val with_subdomains: Boolean
)

data class Operation(
    val action: OperationType,
    val attr: OperationAttribute,
    val value: String,
    val status: OperationStatus
)

data class Grant(
    val user_login: String,
    val user_uid: Long,
    val perm: GrantType,
    val comment: String,
    val partner_data_access: Boolean
)

data class Label(
    val id: Int,
    val name: String
)

data class Webvisor(
    val urls: String,
    val arch_enabled: Boolean,
    val arch_type: WebvisorArchType,
    val load_player_type: WebvisorLoadPlayerType,
    val wv_version: Int,
    val wv_forms: Boolean
)

data class CodeOptions(
    val async: Boolean,
    val informer: Informer,
    val visor: Boolean,
    val track_hash: Boolean,
    val xml_site: Boolean,
    val clickmap: Boolean,
    val in_one_line: Boolean,
    val ecommerce: Boolean,
    val alternative_cdn: Boolean
)

data class Informer(
    val enabled: Boolean,
    val type: InformerType,
    val size: Int,
    val indicator: InformerMetric,
    val color_start: String,
    val color_end: String,
    val color_text: Int,
    val color_arrow: Int
)

data class Monitoring(
    val enable_monitoring: Int,
    val emails: List<String>,
    val enable_sms: Int,
    val sms_time: String,
    val phones: List<String>,
    val phone_ids: List<Long>,
    val possible_phones: List<String>,
    val possible_phone_ids: List<String>
)

data class OfflineOptions(
    val offline_conversion_extended_threshold: Boolean,
    val offline_calls_extended_threshold: Boolean,
    val offline_visits_extended_threshold: Boolean
)

data class PublisherOptions(
    val enabled: Boolean,
    val schema: PublisherSchema,
    val schema_options: List<PublisherSchema>
)

data class CounterFlags(
    val use_in_benchmarks: Boolean,
    val direct_allow_use_goals_without_access: Boolean,
    val collect_first_party_data: Boolean
)

// Enum classes for enumerations
enum class FilterAttribute { /* ... */ }
enum class FilterType { /* ... */ }
enum class FilterAction { /* ... */ }
enum class FilterStatus { /* ... */ }
enum class OperationType { /* ... */ }
enum class OperationAttribute { /* ... */ }
enum class OperationStatus { /* ... */ }
enum class GrantType { /* ... */ }
enum class WebvisorArchType { /* ... */ }
enum class WebvisorLoadPlayerType { /* ... */ }
enum class InformerType { /* ... */ }
enum class InformerMetric { /* ... */ }
enum class PublisherSchema { /* ... */ }
enum class CounterSource { /* ... */ }