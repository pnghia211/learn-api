# Table / DataTable Test Suite & Learning Guide

A progressive testing guide for automated UI verification of modern Table / DataTable components.

---

## Overview Progression Matrix

| Level | Focus Area | Key Concepts & Attributes | Learning Objective |
| :--- | :--- | :--- | :--- |
| **Level 1** | **Structural Verification** | Row count, column count, header text, `data-slot` | Locating rows/cells reliably, verifying baseline structure before behavior. |
| **Level 2** | **Column Resolution & Data Mapping** | `@Header` annotation, reflection, `Map<String,Integer>` | Dynamic column-index resolution instead of hardcoded indices, POJO/record mapping. |
| **Level 3** | **Sorting** | `aria-sort`, before/after row comparison | Full-row snapshot comparison, ascending/descending/reset cycles, stable-sort edge cases. |
| **Level 4** | **Filtering & Column Visibility** | Dropdown toggle, `aria-expanded`, `ColumnOption` enum | Multi-select dropdown state, show/hide columns, filter-vs-search interaction. |
| **Level 5** | **Pagination** | Page size, page index, `TableSection` / `PaginationSection` | Cross-section state sync, row-count-driven assertions, boundary pages. |
| **Level 6** | **Row-Level Interaction** | Row selection, bulk actions, expandable rows | Checkbox state models, indeterminate state, nested/detail row verification. |
| **Level 7** | **Edge Cases & Resilience** | Empty state, loading state, dynamic scroll, stale elements | Handling async data loads, scroll-triggered rendering, staleness during re-render. |

---

## Level 1 — Structural Verification

Focuses on confirming the table renders with the expected shape before testing any behavior.

### 1.1 Verify Header Row Matches Expected Columns
* **Action:** Read all header cell texts in order.
* **Assertion:** Confirm the header list matches the expected column set (order-sensitive or order-agnostic depending on requirement).
* **Key Concept:**
  > **Baseline Contract Check:** Teaches asserting the "shape" of a component before testing its behavior — a table with the wrong columns will produce misleading failures further down the suite.

### 1.2 Verify Row Count Matches Underlying Data Set
* **Action:** Locate all `<tr>` (or `data-slot="row"`) elements within `tbody`.
* **Assertion:** Confirm row count equals the expected dataset size (accounting for pagination page size if applicable).
* **Key Concept:**
  > **Scoped Locators:** Teaches scoping row locators to `tbody` only, avoiding accidental matches inside `thead` or nested component tables.

### 1.3 Verify a Specific Cell Value by Row + Column
* **Action:** Locate a cell using resolved row index and column index.
* **Assertion:** Confirm cell text matches expected value.
* **Key Concept:**
  > **Coordinate-Based Access:** Teaches the fundamental row/column addressing model that every higher-level table test builds on.

---

## Level 2 — Column Resolution & Data Mapping

Focuses on decoupling test code from column order so tests survive column reordering or reuse across similar tables.

### 2.1 Resolve Column Index Dynamically by Header Text
* **Action:** Read header row text at runtime and build a `Map<String, Integer>` of header name → column index.
* **Assertion:** Confirm a value can be retrieved by header name regardless of physical position.
* **Key Concept:**
  > **Dynamic Column Resolution:** Teaches why hardcoded `cells.get(3)` breaks the moment a column is reordered, and how to build resilient lookups instead.

### 2.2 Map a Full Row into a Typed Object
* **Action:** Use annotation-driven reflection (`@Header`) to map a `<tr>` into a Java `record`/POJO.
* **Assertion:** Confirm every field is populated correctly, including type conversion (String → LocalDate, String → int, etc.).
* **Key Concept:**
  > **Reflection-Based Data Binding:** Teaches using `getRecordComponents()` / annotations to eliminate manual field-by-field extraction, and the tradeoff between "magic" convenience and debuggability.

### 2.3 Detect Missing or Null Fields After Mapping
* **Action:** Map a row where one expected column is empty or absent.
* **Assertion:** Confirm the mapper either throws a clear error or the resulting object correctly reflects a null/blank field, rather than silently misaligning columns.
* **Key Concept:**
  > **Fail-Fast Mapping:** Teaches validating structural assumptions at mapping time so failures surface immediately, not three assertions later as a confusing value mismatch.

---

## Level 3 — Sorting

Focuses on verifying that clicking a sortable header actually reorders underlying data correctly, not just that an icon changed.

### 3.1 Sort Ascending and Verify Full-Row Order
* **Action:** Click a sortable column header once.
* **Assertion:** Capture all rows before and after, confirm the column values are in ascending order.
* **Key Concept:**
  > **Value-Based Assertion Over Visual-Only:** Teaches asserting on actual data order rather than trusting an `aria-sort="ascending"` attribute alone — the attribute can be right while the rows are wrong.

### 3.2 Sort Descending, Then Reset to Unsorted
* **Action:** Click the same header a second and third time (asc → desc → default).
* **Assertion:** Confirm each state transition produces the correct row order, and that "unsorted" returns to the original (or a defined default) order.
* **Key Concept:**
  > **State Cycle Verification:** Teaches testing a full toggle cycle instead of only the first click, where regressions often hide in the second/third state.

### 3.3 Sort a Column with Duplicate/Tied Values
* **Action:** Sort a column where multiple rows share the same value.
* **Assertion:** Confirm tied rows remain in a stable, defined secondary order (e.g., original row order preserved).
* **Key Concept:**
  > **Stable Sort Edge Cases:** Teaches that sort correctness isn't just "values increase" — tie-breaking behavior is a real, testable contract.

---

## Level 4 — Filtering & Column Visibility

Focuses on dropdown-driven filtering and show/hide column controls, common sources of state-sync bugs.

### 4.1 Toggle Column Visibility via Dropdown
* **Setup:** A `ColumnOption` enum representing each toggleable column, with distinct `dropdownLabel` vs `headerLabel` values.
* **Action:** Open the column-visibility dropdown, uncheck a column.
* **Assertion:** Confirm the column disappears from the table header **and** all row cells, using the header label — not the dropdown label — to locate it afterward.
* **Key Concept:**
  > **Label Duality:** Teaches handling components where the control-facing label and the table-facing label differ, and why conflating them causes false failures.

### 4.2 Guard Dropdown Open/Close State
* **Action:** Click the dropdown trigger to open it, then attempt an action, then close it.
* **Assertion:** Confirm dropdown open state via `aria-expanded="true"/"false"` before interacting with dropdown items, preventing race conditions where clicks land before the menu renders.
* **Key Concept:**
  > **State-Gated Interaction:** Teaches waiting on an explicit boolean attribute rather than a fixed sleep, mirroring the `data-state="open"` pattern used in the calendar suite.

### 4.3 Apply a Filter and Verify Row Subset
* **Action:** Enter a filter/search value or select a filter option.
* **Assertion:** Confirm remaining visible rows all satisfy the filter condition, and the row count matches the filtered subset size.
* **Key Concept:**
  > **Subset Verification:** Teaches asserting "every visible row matches" plus "count is correct," since a filter that only removes some but not all invalid rows can pass a shallow check.

---

## Level 5 — Pagination

Focuses on the interaction between the table and its pagination controls, and correctly synchronized state between the two.

### 5.1 Navigate to Next/Previous Page and Verify Row Change
* **Action:** Click "next page" control.
* **Assertion:** Confirm row data changes to the next page's expected slice, and row count matches the configured page size (except possibly the last page).
* **Key Concept:**
  > **Cross-Section Synchronization:** Teaches coordinating a `TableSection` and `PaginationSection` as two objects representing one logical unit of state.

### 5.2 Change Page Size and Verify Row Count Adjusts
* **Action:** Select a different page size from a dropdown (e.g., 10 → 25).
* **Assertion:** Confirm the row count updates immediately and the current page resets or adjusts sensibly (commonly resets to page 1).
* **Key Concept:**
  > **Derived State Recalculation:** Teaches that changing one control (page size) has a ripple effect on another (current page index), and both must be verified.

### 5.3 Verify Boundary Pages (First and Last)
* **Action:** Navigate to the first page and confirm "previous" is disabled; navigate to the last page and confirm "next" is disabled.
* **Assertion:** Confirm boundary controls correctly disable, and the last page shows the correct (possibly partial) row count.
* **Key Concept:**
  > **Boundary Condition Testing:** Teaches that off-by-one bugs in pagination (an extra empty page, or an inaccessible last row) are among the most common table defects.

---

## Level 6 — Row-Level Interaction

Focuses on selection models and expandable/detail rows, where per-row state must be tracked individually and in aggregate.

### 6.1 Select Individual Rows via Checkbox
* **Action:** Click checkboxes on two individual rows.
* **Assertion:** Confirm only those rows show a selected/checked state, and a selection counter (if present) reflects the correct total.
* **Key Concept:**
  > **Per-Row State Tracking:** Teaches maintaining a set of selected row identifiers and verifying UI state matches that set exactly — no more, no less.

### 6.2 Select All / Indeterminate State
* **Action:** Click the header "select all" checkbox, then deselect one individual row.
* **Assertion:** Confirm the header checkbox transitions from checked → indeterminate → unchecked as appropriate.
* **Key Concept:**
  > **Tri-State Checkbox Logic:** Teaches testing the often-overlooked indeterminate state, a frequent source of subtle UI bugs.

### 6.3 Expand a Row to Reveal Detail Content
* **Action:** Click an expand toggle on a row.
* **Assertion:** Confirm a detail/nested row renders with expected content, and collapses correctly on second click.
* **Key Concept:**
  > **Nested Component Scoping:** Teaches scoping locators to the expanded detail region specifically, avoiding collisions with sibling rows' detail content.

---

## Level 7 — Edge Cases & Resilience

Focuses on real-world async and dynamic conditions that expose flaky or incorrect automation, rather than component logic itself.

### 7.1 Verify Empty State Rendering
* **Setup:** Apply a filter or search that yields zero results.
* **Assertion:** Confirm an "empty state" message renders and no stale rows remain visible.
* **Key Concept:**
  > **Negative State Coverage:** Teaches that "no data" is a first-class state requiring its own explicit test, not just an assumed side effect.

### 7.2 Handle Loading State Before Data Populates
* **Action:** Trigger an action that causes an async data refetch (sort, filter, page change).
* **Assertion:** Confirm the test correctly waits past any loading/skeleton state rather than asserting against stale or partially-rendered rows.
* **Key Concept:**
  > **Async-Aware Waiting:** Teaches building explicit `ExpectedCondition`s around loading indicators rather than relying on implicit waits, which can pass prematurely against a skeleton UI.

### 7.3 Scroll-Triggered Rendering with Virtualized Rows
* **Action:** Scroll a virtualized/lazy-loaded table until a target row appears.
* **Assertion:** Confirm the target row is found without relying on a captured row count taken before the scroll started.
* **Key Concept:**
  > **Stale Capture Avoidance:** Teaches explicitly re-querying row count inside the scroll loop (capturing `beforeCount` fresh each iteration) rather than reusing a value captured once outside the loop — the exact class of bug already caught in the pagination scroll fix.

### 7.4 Recover from Stale Element References During Re-render
* **Action:** Hold a reference to a row/cell element, trigger a re-render (sort/filter/refresh), then interact with the held reference.
* **Assertion:** Confirm the test either re-locates the element or explicitly asserts a `StaleElementReferenceException` is handled, rather than intermittently failing.
* **Key Concept:**
  > **DOM Re-render Awareness:** Teaches that any action which causes the framework (Vue/React) to re-render invalidates prior element handles, and locators must be re-resolved rather than cached across state changes.
