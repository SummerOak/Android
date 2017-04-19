package casetest;

/**
 * 本地新增 & 云端新增
 * 
 * last:AB			1
 * cloud:新增C：ACB	2
 * local: 新增D: ABD	3
 * expect: ACBD
 * 
 * @author chedifier
 *
 */
public class BookmardCase5_cloud_local_add extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
					.local_next("B")
					.order_time(1L)
					.cloud_next("C")
					.cloud_order_time(2L)
					.dirty(0)
					.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.local_next("D")
				.order_time(3L)
				.cloud_next(null)
				.cloud_order_time(1L)
				.dirty(0)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.local_next(null)
				.order_time(0L)
				.cloud_next("B")
				.cloud_order_time(2L)
				.dirty(0)
				.build());
		
		caseData.add(new TestBookmarkBuilder("D")
				.local_next(null)
				.order_time(3L)
				.cloud_next(null)
				.cloud_order_time(0L)
				.dirty(0)
				.build());
	}

}
