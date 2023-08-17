import { TUserRole } from './userTypes';

export interface IStakeholder {
  id: string;
  name: string;
  email: string;
  roles: TUserRole;
  authorized: boolean;
}

export interface IEruaMember {
  id: string;
  name: string;
  email: string;
  roles: TUserRole;
}

interface IBaseSolution {
  id: number;
  name: string;
  description: string;
  owner: IStakeholder | IEruaMember;
}
export interface IStakeholderProblem extends IBaseSolution {
  keywordList: string[];
}

export interface IEruaMemberSolution extends IBaseSolution {
  typeOfSolution: string;
}

export interface IAllSolutionsResponse<T> {
  totalItems: number;
  solutions: T;
  totalPages: number;
  currentPage: number;
}

export interface IUserResponse {
  id: string;
  name: string;
  email: string;
  roles: string;
}

export interface IProblemPayload {
  [key: string]: string | string[];
  name: string;
  description: string;
  keywordList: string[];
}

export interface ISolutionPayload {
  [key: string]: string;
  name: string;
  description: string;
  typeOfSolution: string;
}

export interface IExhibitionImage {
  id: number;
  name: string;
  owner: IEruaMember;
}
export interface IExhibitionResponse {
  id: number;
  name: string;
  description: string;
  eventDate: string;
  organizer: IEruaMember;
  exhibitionImageList: IExhibitionImage[];
}
