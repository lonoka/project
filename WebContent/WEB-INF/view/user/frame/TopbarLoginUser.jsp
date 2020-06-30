<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<ul class="site-menu main-menu js-clone-nav mr-auto d-none d-lg-block">
	<li><a href="/Logoutbtn.do" class="nav-link">Logout</a></li>
	<li><a href="javascript:void(0)" class="nav-link"
		onclick="getUserInfo()" data-toggle="modal"
		data-target="#mypage_modal" data-backdrop="static">mypage</a></li>
	<li><a href="javascript:void(0)" class="nav-link"
		data-toggle="modal" data-target="#contact_modal"
		data-backdrop="static">Contact</a></li>
</ul>
<script>
	function getUserInfo(){
		$.ajax({
			url:"/getUserInfo.do",
			type :"post",
			dataType : "JSON",
			data : {'user_id' : '<%=userId%>'
			},
			success : function(json) {
				if (json.user_id == '로그인오류') {

				} else {
					$('#user_mod_id').val(json.user_id);
					$('#user_mod_name').val(json.user_name);
					$('#user_mod_mail').val(json.user_mail);
				}
			}
		})
	}
</script>