/**
 * The partition scheme for partitioning ended and active records.
 * The first partition group must be the ended-records since the range right is used (< '9999-12-31 00:00:00.000').
 */
CREATE PARTITION SCHEME [PS_DV_RecordEndDateTime]
	AS PARTITION [PF_DV_RecordEndDateTime]
	TO ([FG_DV_EndedRecords], [FG_DV_ActiveRecords]);