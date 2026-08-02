# Calendar & DatePicker Test Suite & Learning Guide

A progressive testing guide for automated UI verification of modern Calendar and DatePicker components.

---

## Overview Progression Matrix

| Level | Focus Area | Key Concepts & Attributes | Learning Objective |
| :--- | :--- | :--- | :--- |
| **Level 1** | **Basic Single-Date Selection** | `data-selected`, `data-today`, `data-disabled` | Locating cells precisely, verifying boolean DOM state, negative assertion patterns. |
| **Level 2** | **Navigation & Views** | `nextMonth`, `prevMonth`, View Controls | Dynamic heading assertion, year boundary rollover, multi-view conditional locators. |
| **Level 3** | **Multi-Value & Ranges** | `multiple`, `data-highlighted`, `allow-non-contiguous-ranges` | Set state assertions, range endpoints vs. in-range cells, handling split ranges. |
| **Level 4** | **Constraints & Boundaries** | `min-value`, `max-value`, `maximum-days` | Cross-element enforcement (cells + nav controls), numeric limit clipping. |
| **Level 5** | **Composite Widgets** | Popovers, Presets, Dynamic Date Math | Cross-component state sync, dynamic date math, external control integration. |

---

## Level 1 — Basic Single-Date Selection

Focuses on fundamental DOM element interaction, locating target day cells accurately, and asserting standard component attributes.

### 1.1 Select a Specific Day and Verify Selection State
* **Action:** Click a day cell in the current month view.
* **Assertion:** Verify that the cell carries `data-selected="true"` (per theme config `cellTrigger`).
* **Key Concept:**
  > **Precise Cell Locating:** Teaches locating cells by full date or specific DOM scope (`[data-date="..."]`) rather than plain day text, since adjacent grayed-out months often display duplicate numbers (e.g., "31").

### 1.2 Verify "Today" is Visually Marked Without Selecting It
* **Action:** Locate the cell with `data-today="true"`.
* **Assertion:** Confirm `data-selected="true"` is **false** unless explicitly picked.
* **Key Concept:**
  > **Attribute Differentiation:** Teaches distinguishing between two overlapping boolean states on a single DOM element.

### 1.3 Assert a Disabled Date Cannot Be Selected
* **Action:** Click a disabled cell (`data-disabled="true"` configured via `is-date-disabled`).
* **Assertion:** Confirm selection state does not change.
* **Key Concept:**
  > **Negative Outcome Verification:** Teaches proving a click action had zero side effects rather than just checking element existence.

---

## Level 2 — Navigation

Focuses on state transitions across months, years, and view hierarchies (Day → Month → Year).

### 2.1 Navigate Month Controls & Verify Heading Updates
* **Action:** Click "next month" (`nextMonth` button).
* **Assertion:** Confirm heading label text updates dynamically (e.g., from `"July 2026"` to `"August 2026"`).
* **Key Concept:**
  > **Dynamic Headings & Icon Locators:** Teaches text assertions on dynamic headers and locating icon-only buttons via `aria-label` or DOM position.

### 2.2 Navigate Across a Year Boundary
* **Action:** From December of the current year, click "next month".
* **Assertion:** Confirm the heading correctly rolls over to January of the following year (e.g., `"January 2027"`).
* **Key Concept:**
  > **Temporal Rollover Edge Cases:** Tests year-end date logic where rollover bugs typically occur.

### 2.3 Switch Views (Day View → Month View → Year View)
* **Action:** Click heading (when `view-control` is enabled) to drill up from Day Grid → Month Grid → Year Grid, then select a month/year to drill back down.
* **Assertion:** Confirm active grid and locators adjust correctly at each level.
* **Key Concept:**
  > **Dynamic Page Object Views:** Handles components with non-static structures by detecting the current view state before evaluating cell locators.

---

## Level 3 — Multi-Value and Range Selection

Focuses on complex selection models involving multiple distinct dates or connected range bands.

### 3.1 Select Multiple Non-Contiguous Dates
* **Setup:** Calendar configured with `multiple` prop enabled.
* **Action:** Click three separate non-adjacent days.
* **Assertion:** Confirm all three (and **only** those three) show `data-selected="true"`.
* **Key Concept:**
  > **Set State Verification:** Teaches evaluating an entire collection of elements against an expected set rather than single-element checks.

### 3.2 Select a Date Range by Clicking Start then End
* **Action:** Click a start date, then an end date.
* **Assertion:** Confirm start/end endpoints and all intermediate cells carry the expected highlighted attributes (e.g., `data-selected`, `data-highlighted`).
* **Key Concept:**
  > **Theme State Mapping:** Teaches reading component theme configurations to differentiate range endpoints from in-range fills.

### 3.3 Attempt a Range Across Unavailable Dates
* **Setup:** Configured with `is-date-unavailable` + `allow-non-contiguous-ranges`.
* **Action:** Attempt a range selection spanning an unavailable date.
* **Assertion:** Confirm the selection either rejects or splits into sub-ranges based on the configuration setting.
* **Key Concept:**
  > **Prop-Driven Behavior Variants:** Teaches designing test variants where a single prop fundamentally alters user interaction rules.

---

## Level 4 — Constraints and Boundaries

Focuses on system-enforced boundaries, date limits, and numeric range caps.

### 4.1 Verify Min/Max Date Boundaries are Enforced
* **Setup:** Configured with `min-value` and `max-value`.
* **Action:** Attempt to select dates outside boundaries or navigate past boundary months.
* **Assertion:** Confirm out-of-bound cells carry `data-disabled="true"` and navigation controls disable or no-op.
* **Key Concept:**
  > **Dual-Layer Enforcement:** Verifies constraint rules applied simultaneously to grid cells and macro navigation controls.

### 4.2 Verify Maximum-Days Caps a Range Selection
* **Setup:** Configured with `maximum-days` constraint (e.g., max 7 days).
* **Action:** Attempt to select a range longer than the configured limit.
* **Assertion:** Confirm range clips at the maximum allowed days or prevents completing the selection.
* **Key Concept:**
  > **Numeric Limit Constraints:** Teaches validating business rules commonly found in booking and reservation systems.

---

## Level 5 — Real-World Composite Widgets

Focuses on modern UI integration patterns where calendars interface with popovers, dropdown presets, and external controls.

### 5.1 DatePicker: Popover + Calendar + Button Label Sync
* **Action:** Open popover, select a date, and close it (via click-outside or auto-close).
* **Assertion:** Confirm the trigger button label updates to reflect the formatted date string (e.g., `"Jan 10, 2026"`).
* **Key Concept:**
  > **Cross-Component Sync:** Asserts that internal calendar state changes correctly update external trigger elements across popover lifecycle events.

### 5.2 Date Range Picker with Preset Shortcut Buttons
* **Action:** Click a shortcut button (e.g., `"Last 7 days"`, `"Last 30 days"`).
* **Assertion:** Confirm (a) calendar internally highlights the target range, and (b) trigger button label shows the matching formatted range string.
* **Key Concept:**
  > **Shortcut vs. Manual Parity:** Verifies shortcut presets yield identical state and visual outcomes compared to manual selections.

### 5.3 "Today" External Control Button
* **Action:** Click an external "Today" button outside the calendar container.
* **Assertion:** Confirm displayed month and selection jump to current date.
* **Key Concept:**
  > **Clock-Independent Test Logic:** Teaches writing resilient test scripts using dynamic date calculation (e.g., `LocalDate.now()`) rather than hardcoded static strings.