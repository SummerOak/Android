package casetest;


/**
 * 云端数据错乱：云端新增，存在环
 * 
 * last: AB
 * cloud:
 * 			A->B ; 
 * 			B->NULL;
 * 		    C->D;
 * 			D->C;
 * 
 * local:   B -> A
 * 
 * expect: 	BACD or BADC
 * 
 * @author chedifier
 *
 */
public class BookmarkCase4_clouderror_circle extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next(null)
				.order_time(2L)
				.cloud_next("B")
				.cloud_order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("A")
				.order_time(2L)
				.cloud_next(null)
				.cloud_order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next(null)
				.order_time(0L)
				.cloud_next("D")
				.cloud_order_time(5L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next(null)
				.order_time(0L)
				.cloud_next("C")
				.cloud_order_time(5L)
				.build());
	}

}