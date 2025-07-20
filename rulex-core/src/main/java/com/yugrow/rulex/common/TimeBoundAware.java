package com.yugrow.rulex.common;

import java.time.Duration;

public interface TimeBoundAware {

    Duration getTimeWindow();
}
