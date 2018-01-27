package casetest;

/**
 * 本地修改 && 云端修改
 * 
 * last: ABCDEF					1
 * cloud: 把B放到D后面： ACDBEF		2
 * local: 把 E 放到 B前面： AEBCDF	3
 * 
 * expect: AEBCDF
 * 
 * @author chedifier
 *
 */
public class BookmarkCase3_modify_conflict3 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("E")				
				.order_time(3L)
				.cloud_next("C")
				.cloud_order_time(2L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("C")
				.order_time(1L)
				.cloud_next("E")
				.cloud_order_time(2L)
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
				.local_next("F")
				.order_time(3L)
				.cloud_next("B")
				.cloud_order_time(2L)
				.build());
		caseData.add(new TestBookmarkBuilder("E")
				.dirty(0)
				.local_next("B")
				.order_time(3L)
				.cloud_next("F")
				.cloud_order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("F")
				.dirty(0)
				.local_next(null)
				.order_time(1L)
				.cloud_next(null)
				.cloud_order_time(1L)
				.build());
	}

}
