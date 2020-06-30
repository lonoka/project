<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal fade" id="alert_modal" tabindex="-1" role="dialog"
	aria-labelledby="alertLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="alertLabel">CA.</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<div id="alert_modal_body" class="modal-body"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="search_modal" tabindex="-1" role="dialog" data-keyboard="false"
	data-backdrop="static" aria-labelledby="searchLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="searchLabel">CA.</h4>
			</div>
			<div id="search_modal_body" class="modal-body">검색중입니다.
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirm_modal" tabindex="-1" role="dialog"
	data-backdrop="static" aria-labelledby="confirmLabel"
	aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="confirmLabel">CA.</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="reset_confirm_modal()">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<div id="confirm_modal_body" class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="reset_confirm_modal()">취소</button>

				<button type="button" id="confirm_btn" class="btn btn-primary"
					style="color: dodgerblue;" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>
<script>
	function reset_confirm_modal() {
		$('#confirm_modal_body').html('');
		$('#confirm_btn').removeAttr('onclick');
	}
</script>