package casetest;

/**
 * 云端数据错乱：云端修改，但上传部分未
 * 
 * last: ABCD		1
 * cloud: 把 D 放到A的前面， D->A, C->null ； 但 C这条数据上传失败了 : ABCD		2
 * 
 * local: 未修改ABCD	1
 * 
 * expect: 	ABCD
 * 
 * @author wxj_pc
 *
 */
public class BookmarkCase4_clouderror_partly extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("B")
				.cloud_next("B")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("C")
				.cloud_next("C")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next("D")
				.order_time(1L)
				.cloud_next("D")
				.cloud_order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next(null)
				.order_time(1L)
				.cloud_next("A")
				.cloud_order_time(0L)
				.build());
	
	}

}