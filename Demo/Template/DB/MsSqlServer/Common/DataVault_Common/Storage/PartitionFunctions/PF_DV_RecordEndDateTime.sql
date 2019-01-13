/**
 * The partition function for paritioning satelites into ended and not-ended records.
 * The boundary is the LoadEndDate value for not-ended records ('9999-12-31 00:00:00.00').
 * Since the values are sorted ascending and we want this value the be in its own partition we use RANGE RIGHT.
 * The the last partition are now the not-ended (active) records.
 * Note: The datatype of this partition function must be in sync with the [LoadDateTimeType] user-defined type (since using user-defined types are is not allowed).
 * Note: The format of this function is in this format so the sql schema compare doesn't yield a delta during comparison.
 */
CREATE PARTITION FUNCTION [PF_DV_RecordEndDateTime]
(
	DATETIME2(2)
)
AS RANGE RIGHT
FOR VALUES (N'9999-12-31 00:00:00.00');
--FOR VALUES (N'12/31/9999 00:00:00');