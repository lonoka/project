<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- mypage -->
<div class="modal fade" id="mypage_modal" tabindex="-1" role="dialog"
	aria-labelledby="mypageLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="mypageLabel">Mypage</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="reset_form()">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="modify_form" name="f" method="post">
				<div class="modal-body">
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							id="user_mod_id" name="user_id" placeholder="ID" value=""
							readonly required="required">
					</div>
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							id="user_mod_name" name="user_name" placeholder="이름" value=""
							required="required">
					</div>
					<div class="form-group row">
						<div class="col-sm-6 mb-3 mb-sm-0">
							<input type="password" class="form-control form-control-user"
								id="user_mod_password" name="password" maxlength="16"
								oninput="checkPw()" placeholder="비밀번호"
								style="ime-mode: disabled;" required="required">
						</div>
						<div class="col-sm-6">
							<input type="password" class="form-control form-control-user"
								id="RepeatPassword" name="RepeatPassword" oninput="checkPw2()"
								placeholder="비밀번호 재입력" style="ime-mode: disabled;"
								required="required">
						</div>
						<div id="wrongPw"
							style="display: none; color: red; font-size: 12px; margin-left: 20px;">6~16자의
							비밀번호를 사용해주세요.</div>
						<div id="wrongPw2"
							style="display: none; color: red; font-size: 12px; margin-left: 20px;">비밀번호가
							일치하지 않습니다.</div>
					</div>
					<div class="form-group">
						<input type="email" class="form-control form-control-user"
							id="user_mod_mail" name="user_mail" placeholder="Email" value=""
							readonly required="required">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="reset_form()">Close</button>
					<button type="button" onclick="UserModify()"
						class="btn btn-primary" style="color: dodgerblue;">회원정보
						수정</button>
				</div>

			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	var pwCheck1 = 0;
	var pwCheck2 = 0;
	function reset_form() {
		$('#wrongPw').hide();
		$('#wrongPw2').hide();
		$('#user_mod_id').val('');
		$('#user_mod_name').val('');
		$('#user_mod_password').val('');
		$('#RepeatPassword').val('');
		$('#user_mod_mail').val('');
	}
	function checkPw() {
		var inputed = f.password.value;
		if (inputed.length < 6) {
			$('#wrongPw').show();
			$('#wrongPw2').hide();
		} else {
			pwCheck1 = 1;
			$('#wrongPw').hide();
			$('#wrongPw2').hide();
		}
	}
	function checkPw2() {
		var inputed = f.password.value;
		var inputed2 = f.RepeatPassword.value;
		if (inputed != inputed2) {
			$('#wrongPw2').show();
		} else {
			pwCheck2 = 1;
			$('#wrongPw2').hide();
		}
	}
	function UserModify() {
		if (doModifyUserCheck(document.getElementById('modify_form'))) {
			$.ajax({
				url : "/updateUserInfo.do",
				type : "post",
				data : {
					'user_id' : $('#user_mod_id').val(),
					'user_name' : $('#user_mod_name').val(),
					'password' : $('#user_mod_password').val()
				},
				success : function(a) {
					console.log(a);
					reset_form();
					if (a == 1) {
						$('#alert_modal_body').html('회원정보 수정에 성공하였습니다.');
					} else if (a == 0) {
						$('#alert_modal_body').html('회원정보 수정에 실패하였습니다.');
					}
					reset_form();
					$('#alert_modal').modal('show')
					$('#mypage_modal').modal('hide');

				}
			})
		}
	}
	function doModifyUserCheck(f) {
		if (f.password.value.length < 6) {
			$('#alert_modal_body').html('6~16자의 비밀번호를 사용해주세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.password.value.length > 16) {
			$('#alert_modal_body').html('6~16자의 비밀번호를 사용해주세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.user_id.value == "") {
			$('#alert_modal_body').html('아이디를 입력하세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.user_name.value == "") {
			$('#alert_modal_body').html('이름을 입력하세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.password.value == "") {
			$('#alert_modal_body').html('비밀번호를 입력하세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.RepeatPassword.value == "") {
			$('#alert_modal_body').html('비밀번호 재입력을 입력하세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.user_mail.value == "") {
			$('#alert_modal_body').html('Email을 입력하세요.');
			$('#alert_modal').modal('show')
			return false;
		}
		if (f.password.value != f.RepeatPassword.value) {
			$('#alert_modal_body').html('비밀번호가 같지 않습니다.');
			$('#alert_modal').modal('show')
			return false;
		}
		return true;
	}
</script>