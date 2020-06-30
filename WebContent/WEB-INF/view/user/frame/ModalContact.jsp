<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal fade" id="contact_modal" tabindex="-1" role="dialog"
	aria-labelledby="contactLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="contactLabel">Contact</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<form class="" method="post">
				<div class="modal-body">
					<div class="col-md-12" style="margin: auto;">
						<div class="control-group form-group">
							<div class="controls">
								<label>Full Name:</label> <input class="form-control"
									id="contactName" name="contactName" required type="text">
								<p class="help-block"></p>
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Phone Number:</label> <input class="form-control"
									id="contactTel" name="contactTel" required type="tel" onkeyup="phoneNumberFormat(this)">
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Email Address:</label> <input class="form-control"
									id="contactEmail" name="contactEmail" required type="email">
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Message:</label>
								<textarea class="form-control" cols="100" name="contactMessage"
									id="contactMessage" maxlength="999" required rows="10"
									style="resize: none"></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" id="contact_btn" onclick="sendContact()"
						class="btn btn-primary" style="color: dodgerblue;">Send
						Message</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	function phoneNumberFormat(obj) {
		obj.value = obj.value.replace(/[^0-9\-]/g, "");
		var number = obj.value.replace(/[^0-9]/g, "");
		var tel = "";

		// 서울 지역번호(02)가 들어오는 경우
		if (number.substring(0, 2).indexOf('02') == 0) {
			$("#user_tel").attr("maxlength", "12")
			if (number.length < 3) {
				return number;
			} else if (number.length < 6) {
				tel += number.substr(0, 2);
				tel += "-";
				tel += number.substr(2);
			} else if (number.length < 10) {
				tel += number.substr(0, 2);
				tel += "-";
				tel += number.substr(2, 3);
				tel += "-";
				tel += number.substr(5);
			} else {
				tel += number.substr(0, 2);
				tel += "-";
				tel += number.substr(2, 4);
				tel += "-";
				tel += number.substr(6);
			}

			// 서울 지역번호(02)가 아닌경우
		} else {
			$("#user_tel").attr("maxlength", "13")
			if (number.length < 4) {
				return number;
			} else if (number.length < 7) {
				tel += number.substr(0, 3);
				tel += "-";
				tel += number.substr(3);
			} else if (number.length < 11) {
				tel += number.substr(0, 3);
				tel += "-";
				tel += number.substr(3, 3);
				tel += "-";
				tel += number.substr(6);
			} else {
				tel += number.substr(0, 3);
				tel += "-";
				tel += number.substr(3, 4);
				tel += "-";
				tel += number.substr(7);
			}
		}

		obj.value = tel;
		$(obj).focusout();
	}
	function sendContact() {
		if ($('#contactName').val() == '') {
			$('#alert_modal_body').html('이름을 입력해주세요.');
			$('#alert_modal').modal('show')
		} else if ($('#contactTel').val() == '') {
			$('#alert_modal_body').html('전화번호를 입력해주세요.');
			$('#alert_modal').modal('show')
		} else if ($('#contactEmail').val() == '') {
			$('#alert_modal_body').html('이메일을 입력해주세요.');
			$('#alert_modal').modal('show')
		} else if ($('#contactMessage').val() == '') {
			$('#alert_modal_body').html('문의내용을 입력해주세요.');
			$('#alert_modal').modal('show')
		} else {
			$('#contact_btn').attr('disabled', true);
			$.ajax({
				url : "/contactSend.do",
				type : "post",
				data : {
					'contactName' : $('#contactName').val(),
					'contactTel' : $('#contactTel').val(),
					'contactEmail' : $('#contactEmail').val(),
					'contactMessage' : $('#contactMessage').val()
				},
				success : function(a) {
					$('#contact_btn').attr('disabled', false);
					if (a == "1") {
						$('#alert_modal_body').html('문의메일이 발송되었습니다.');
						$('#alert_modal').modal('show');
						$('#contact_modal').modal('hide');
						$('#contactName').val('');
						$('#contactTel').val('');
						$('#contactEmail').val('');
						$('#contactMessage').val('');
					} else if (a == "0") {
						$('#alert_modal_body').html(
								'일시적 오류가 발생하였습니다. 나중에 다시 시도해주세요.');
						$('#alert_modal').modal('show');
						$('#contact_modal').modal('hide');
						$('#contactName').val('');
						$('#contactTel').val('');
						$('#contactEmail').val('');
						$('#contactMessage').val('');
					}
				}
			})
		}
	}
</script>
