<?php
require "con.php";

$username = $_POST["username"];
$email = $_POST["email"];
$password = $_POST["psw"];
$mobile = $_POST["mobile"];
$gender = $_POST["gender"];

// $username = "fn";
// $email = "fode@gmail.com";
// $password = "passer";
// $mobile = "772220022";
// $gender = "homme";

$isValidEmail = filter_var($email, FILTER_VALIDATE_EMAIL);
if($conn) {
	if(strlen($password) > 40 || strlen($password) < 6) {
		echo "Le mot de passe doit être inférieur à 40 caractères et supérieur à 6 caractères. ";
	}
	else if($isValidEmail == false){
		echo "L'Email n'est pas valide.";
	}
	else {
		$sqlCheckUsername = "SELECT * FROM  `users` WHERE `pseudo` LIKE '$username' ";
		$usernameQuery = mysqli_query($conn, $sqlCheckUsername);
		
		$sqlCheckEmail = "SELECT * FROM  `users` WHERE `email` LIKE '$email' ";
		$emailQuery = mysqli_query($conn, $sqlCheckEmail);
		
		if(mysqli_num_rows($usernameQuery) > 0) {
			echo "Le nom d'utilisateur existe déja";
		}
		elseif(mysqli_num_rows($emailQuery) > 0) {
			echo "Cet Email existe déja";
		}
		else{
			$sql_register = "INSERT INTO `users`(`pseudo`,`email`,`password`,`mobile`,`sexe`) VALUES ('$username','$email','$password','$mobile','$gender')";
			if(mysqli_query($conn,$sql_register)){
				echo "Successfully Registered";
			}
			else {
				echo "Inscription Echouée";
			}
		}
	}
}
else {
	echo "Erreur connexion.";
}


?>