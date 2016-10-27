package ccg.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;


public class OLI_LU_STATE {

	private List<String> dataList = new ArrayList<String>();
	
	public OLI_LU_STATE(){
		initData();
		
	}
	public List<String> loadData(){
		return this.dataList;
		
		
	}

	private void initData() {
		this.dataList.add("0=Unknown");
		this.dataList.add("1=Alabama");
		this.dataList.add("2=Alaska");
		this.dataList.add("4=Arizona");
		this.dataList.add("5=Arkansas");
		this.dataList.add("6=California");
		this.dataList.add("7=Colorado");
		this.dataList.add("8=Connecticut");
		this.dataList.add("9=Delaware");
		this.dataList.add("10=District of Columbia");
		this.dataList.add("12=Florida");
		this.dataList.add("13=Georgia");
		this.dataList.add("14=Guam");
		this.dataList.add("15=Hawaii");
		this.dataList.add("16=Idaho");
		this.dataList.add("17=Illinois");
		this.dataList.add("18=Indiana");
		this.dataList.add("19=Iowa");
		this.dataList.add("20=Kansas");
		this.dataList.add("21=Kentucky");
		this.dataList.add("22=Louisiana");
		this.dataList.add("23=Maine");
		this.dataList.add("24=Marshall Islands");
		this.dataList.add("25=Maryland");
		this.dataList.add("26=Massachusetts");
		this.dataList.add("27=Michigan");
		this.dataList.add("28=Minnesota");
		this.dataList.add("29=Mississippi");
		this.dataList.add("30=Missouri");
		this.dataList.add("31=Montana");
		this.dataList.add("32=Nebraska");
		this.dataList.add("33=Nevada");
		this.dataList.add("34=New Hampshire");
		this.dataList.add("35=New Jersey");
		this.dataList.add("36=New Mexico");
		this.dataList.add("37=New York");
		this.dataList.add("38=North Carolina");
		this.dataList.add("39=North Dakota");
		this.dataList.add("40=Northern Mariana Islands");
		this.dataList.add("41=Ohio");
		this.dataList.add("42=Oklahoma");
		this.dataList.add("43=Oregon");
		this.dataList.add("44=Palau Island");
		this.dataList.add("45=Pennsylvania");
		this.dataList.add("46=Puerto Rico");
		this.dataList.add("47=Rhode Island");
		this.dataList.add("48=South Carolina");
		this.dataList.add("49=South Dakota");
		this.dataList.add("50=Tennessee");
		this.dataList.add("51=Texas");
		this.dataList.add("52=Utah");
		this.dataList.add("53=Vermont");
		this.dataList.add("54=Virgin Islands");
		this.dataList.add("55=Virginia");
		this.dataList.add("56=Washington");
		this.dataList.add("57=West Virginia");
		this.dataList.add("58=Wisconsin");
		this.dataList.add("59=Wyoming");
		this.dataList.add("60=Armed Forces Americas (except Canada)");
		this.dataList.add("61=Armed Forces Canada, Africa, Europe, Middle East");
		this.dataList.add("62=US Armed Forces Pacific");
		this.dataList.add("80=Guantanamo Bay (US Naval Base) Cuba");
/*		'101=Alberta', 
		'102=British Columbia', 
		'103=Manitoba', 
		'104=New Brunswick', 
		'105=AKA NL Newfoundland and Labrador', 
		'106=Northwest Territories', 
		'107=Nova Scotia', 
		'108=Ontario', 
		'109=Prince Edward Island', 
		'110=Quebec', '111=Saskatchewan', 
		'112=Yukon Territory', 
		'113=Nunavut', 
		'201=Australian Capital Territory', 
		'202=New South Wales', 
		'203=Northern Territory', 
		'204=Queensland', 
		'205=South Australia', 
		'206=Tasmania', 
		'207=Victoria', 
		'208=Western Australia', 
		'301=Aichi', 
		'302=Akita', 
		'303=Aomori', 
		'304=Chiba', 
		'305=Ehime', 
		'306=Fukui', 
		'307=Fukuoka', 
		'308=Fukushima', 
		'309=Gifu', 
		'310=Gunma', 
		'311=Hiroshima', 
		'312=Hokkaido', 
		'313=Hyogo', 
		'314=Ibaraki', 
		'315=Ishikawa', 
		'316=Iwate', 
		'317=Kagawa', 
		'318=Kagoshima', 
		'319=Kanagawa', 
		'320=Kouchi', 
		'321=Kumamoto', 
		'322=Kyoto', 
		'323=Mie', 
		'324=Miyagi', 
		'325=Miyazaki', 
		'326=Nagano', 
		'327=Nagasaki', 
		'328=Nara', 
		'329=Niigata', 
		'330=Oita', 
		'331=Okayama', 
		'332=Okinawa', 
		'333=Osaka', 
		'334=Saga', 
		'335=Saitama', 
		'336=Shiga', 
		'337=Shimane', 
		'338=Shizuoka', 
		'339=Tochigi', 
		'340=Tokushima', 
		'341=Tokyo', 
		'342=Tottori', 
		'343=Toyama', 
		'344=Wakayama', 
		'345=Yamagata', 
		'346=Yamaguchi', 
		'347=Yamanashi', 
		'401=Aguascalientes', 
		'402=Baja California', 
		'403=Baja California Sur', 
		'404=Campeche', 
		'405=Chiapas', 
		'406=Chihuahua', 
		'407=Coahuila', 
		'408=Colima', 
		'409=Distrito Federal', 
		'410=Durango', 
		'411=Guanajuato', 
		'412=Guerrero', 
		'413=Hidalgo', 
		'414=Jalisco', 
		'415=Mexico', 
		'416=Michoacan', 
		'417=Morelos', 
		'418=Nayarit', 
		'419=Nuevo Leon', 
		'420=Oaxaca', 
		'421=Puebla', 
		'422=Queretaro', 
		'423=Quintana Roo', 
		'424=San Luis Potos', 
		'425=Sinaloa', 
		'426=Sonora', 
		'427=Tabasco', 
		'428=Tamaulipas', 
		'429=Tlaxcala', 
		'430=Veracruz', 
		'431=Yucatán', 
		'432=Zacatecas', 
		'501=Gauteng', 
		'502=Western Cape', 
		'503=Northern Province', 
		'504=Northwest Province', 
		'505=Kwa Zulu Natal', 
		'506=Eastern Cape', 
		'507=Freestate', 
		'508=Northern Cape', 
		'509=Mpumalanga', 
		'601=Guernsey [Guernesey]', 
		'602=Jersey', 
		'611=Antrim', 
		'612=Ards', 
		'613=Armagh', 
		'614=Ballymena', 
		'615=Ballymoney', 
		'616=Banbridge', 
		'617=Belfast', 
		'618=Carrickfergus', 
		'619=Castlereagh', 
		'620=Coleraine', 
		'621=Cookstown', 
		'622=Craigavon', 
		'623=Derry', 
		'624=Down', 
		'625=Dungannon', 
		'626=Fermanagh', 
		'627=Larne', '628=Limavady', '629=Lisburn', '630=Magherafelt', '631=Moyle', '632=Newry and Mourne', '633=Newtownabbey', '634=North Down', '635=Omagh', '636=Strabane', '651=Aberdeen City', '652=Aberdeenshire', '653=Angus', '654=Argyll and Bute', '655=Clackmannanshire', '656=Dumfries and Galloway', '657=Dundee City', '658=East Ayrshire', '659=East Dunbartonshire', '660=East Lothian', '661=East Renfrewshire', '662=Edinburgh, City of', '663=Eilean Siar', '664=Falkirk', '665=Fife', '666=Glasgow City', '667=Highland', '668=Inverclyde', '669=Midlothian', '670=Moray', '671=North Ayrshire', '672=North Lanarkshire', '673=Orkney Islands', '674=Perth and Kinross', '675=Renfrewshire', '676=Scottish Borders, The', '677=Shetland Islands', '678=South Ayrshire', '679=South Lanarkshire', '680=Stirling', '681=West Dunbartonshire', '682=West Lothian', '701=Blaenau Gwent', '702=Bridgend', '703=Caerphilly', '704=Cardiff', '705=Carmarthenshire', '706=Ceredigion [Sir Ceredigion]', '707=Conwy', '708=Denbighshire', '709=Flintshire', '710=Gwynedd', '711=Isle of Anglesey', '712=Merthyr Tydfil', '713=Monmouthshire', '714=Neath Port Talbot', '715=Newport', '716=Pembrokeshire', '717=Powys', '718=Rhondda, Cynon, Taff', '719=Swansea', '720=Torfaen', '721=Vale of Glamorgan, The', '722=Wrexham', '741=Dorset', '742=Bolton', '743=Bury', '744=Manchester', '745=Oldham', '746=Rochdale', '747=Salford', '748=Stockport', '749=Tameside', '750=Trafford', '751=Wigan', '752=Barking and Dagenham', '753=Barnet', '754=Bexley', '755=Brent', '756=Bromley', '757=Camden', '758=Croydon', '759=Ealing', '760=Enfield', '761=Greenwich', '762=Hackney', '763=Hammersmith and Fulham', '764=Haringey', '765=Harrow', '766=Havering', '767=Hillingdon', '768=Hounslow', '769=Islington', '770=Kensington and Chelsea', '771=Kingston upon Thames', '772=Lambeth', '773=Lewisham', '774=Merton', '775=Newham', '776=Redbridge', '777=Richmond upon Thames', '778=Southwark', '779=Sutton', '780=Tower Hamlets', '781=Waltham Forest', '782=Wandsworth', '783=Westminster', '784=Knowsley', '785=Liverpool', '786=Sefton', '787=St. Helens', '788=Wirral', '789=Bedfordshire', '790=Buckinghamshire', '791=Cambridgeshire', '792=Cheshire', '793=Cornwall', '794=Cumbria', '795=Derbyshire', '796=Devon', '797=Durham', '798=East Sussex', '799=Essex', '800=Gloucestershire', '801=Hampshire', '802=Hertfordshire', '803=Kent', '804=Lancashire', '805=Leicestershire', '806=Lincolnshire', '807=Norfolk', '808=North Yorkshire', '809=Northamptonshire', '810=Northumberland', '811=Nottinghamshire', '812=Oxfordshire', '813=Shropshire', '814=Somerset', '815=Staffordshire', '816=Suffolk', '817=Surrey', '818=Warwickshire', '819=West Sussex', '820=Wiltshire', '821=Worcestershire', '822=Barnsley', '823=Doncaster', '824=Rotherham', '825=Sheffield', '826=London, City of', '827=Gateshead', '828=Newcastle upon Tyne', '829=North Tyneside', '830=South Tyneside', '831=Sunderland', '832=Bath and North East Somerset', '833=Blackburn with Darwen', '834=Blackpool', '835=Bournemouth', '836=Bracknell Forest', '837=Brighton and Hove', '838=Bristol, City of', '839=Darlington', '840=Derby', '841=East Riding of Yorkshire', '842=Halton', '843=Hartlepool', '844=Herefordshire, County of', '845=Isle of Wight', '846=Isles of Scilly', '847=Kingston upon Hull, City of', '848=Leicester', '849=Luton', '850=Medway', '851=Middlesbrough', '852=Milton Keynes', '853=North East Lincolnshire', '854=North Lincolnshire', '855=North Somerset', '856=Nottingham', '857=Peterborough', '858=Plymouth', '859=Poole', '860=Portsmouth', '861=Reading', '862=Redcar and Cleveland', '863=Rutland', '864=Slough', '865=South Gloucestershire', '866=Southampton', '867=Southend-on-Sea', '868=Stockton-on-Tees', '869=Stoke-on-Trent', '870=Swindon', '871=Telford and Wrekin', '872=Thurrock', '873=Torbay', '874=Warrington', '875=West Berkshire', '876=Windsor and Maidenhead', '877=Wokingham', '878=York', '879=Birmingham', '880=Coventry', '881=Dudley', '882=Sandwell', '883=Solihull', '884=Walsall', '885=Wolverhampton', '886=Bradford', '887=Calderdale', '888=Kirklees', '889=Leeds', '890=Wakefield', '1000=All states', '2147483647=Other'
		*/
	}
}