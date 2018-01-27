package casetest;

/**
 * 云端新增
 * 
 * last:AB			1
 * cloud:新增C：ACB	1
 * local: 无修改: AB	2
 * expect: ACB
 * 
 * @author chedifier
 *
 */
public class BookmarkCase2_cloud_add_middle extends TestCase{

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
				.local_next(null)
				.order_time(1L)
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
	}

}