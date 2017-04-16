package casetest;


/**
 * 本地修改 && 云端修改&云端新增
 * 
 * last: ABCD
 * cloud: EBACD
 * local: ABDC
 * 
 * expect: EBADC
 * 
 * @author chedifier
 *
 */
public class BookmarkCase4 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("B")
				.cloud_next("C")
				.order_time(2L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("D")
				.cloud_next("A")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next(null)
				.cloud_next("D")
				.order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next("C")
				.cloud_next(null)
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("E")
				.dirty(0)
				.local_next(null)
				.cloud_next("A")
				.order_time(1L)
				.build());
	}

}