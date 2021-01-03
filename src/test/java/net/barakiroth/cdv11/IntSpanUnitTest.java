package net.barakiroth.cdv11;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntSpanUnitTest {

    @Test
    void when_querying_for_containment_in_an_empty_span_then_the_answer_should_always_be_false() {
        final IntSpan intSpan = IntSpan.of(10, 0);
        assertThat(intSpan).isEqualTo(IntSpan.EMPTY);
        assertThat(intSpan.contains(-789)).isFalse();
        assertThat(intSpan.contains(-1)).isFalse();
        assertThat(intSpan.contains(0)).isFalse();
        assertThat(intSpan.contains(10)).isFalse();
        assertThat(intSpan.contains(11)).isFalse();
        assertThat(intSpan.contains(123)).isFalse();
    }

    @Test
    void when_querying_for_containment_in_span_then_the_answer_should_be_as_expected() {
        final IntSpan intSpan = IntSpan.of(0, 0);
        assertThat(intSpan).isNotEqualTo(IntSpan.EMPTY);
        assertThat(intSpan.contains(-1)).isFalse();
        assertThat(intSpan.contains(0)).isTrue();
        assertThat(intSpan.contains(1)).isFalse();
    }

    @Test
    void when_querying_next_in_an_empty_span_then_the_answer_should_be_null() {
        final IntSpan intSpan = IntSpan.EMPTY;
        assertThat(intSpan.next(0)).isNull();
    }

    @Test
    void when_querying_next_in_a_single_element_span_then_the_returned_element_should_be_same() {
        final IntSpan intSpan = IntSpan.of(77, 77);
        assertThat(intSpan.next(0)).isEqualTo(77);
        assertThat(intSpan.next(76)).isEqualTo(77);
        assertThat(intSpan.next(77)).isEqualTo(77);
        assertThat(intSpan.next(78)).isEqualTo(77);
    }
}
